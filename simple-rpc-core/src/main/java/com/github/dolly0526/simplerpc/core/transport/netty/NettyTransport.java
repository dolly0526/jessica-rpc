package com.github.dolly0526.simplerpc.core.transport.netty;

import com.github.dolly0526.simplerpc.core.client.response.InFlightRequests;
import com.github.dolly0526.simplerpc.core.transport.Transport;
import com.github.dolly0526.simplerpc.core.transport.protocol.Command;
import com.github.dolly0526.simplerpc.core.client.response.ResponseFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author yusenyang
 * @create 2021/3/8 20:28
 */
public class NettyTransport implements Transport {

    private final Channel channel;
    private final InFlightRequests inFlightRequests;


    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }


    @Override
    public CompletableFuture<Command> send(Command request) {

        // 构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();

        try {
            // 将在途请求放到inFlightRequests中
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));

            // 发送命令
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                // 处理发送失败的情况
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            // 处理发送异常
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }

        return completableFuture;
    }
}
