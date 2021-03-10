package com.github.dolly0526.jessicarpc.core.transport.netty.dispatcher;

import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author yusenyang
 * @create 2021/3/8 20:28
 */
public class NettyTransport implements Transport {

    private final Channel channel;
    private final RequestPool requestPool;


    public NettyTransport(Channel channel, RequestPool requestPool) {
        this.channel = channel;
        this.requestPool = requestPool;
    }


    @Override
    public CompletableFuture<Command> send(Command request) {

        // 构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();

        try {
            // 将在途请求放到inFlightRequests中
            requestPool.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));

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
            requestPool.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }

        return completableFuture;
    }
}
