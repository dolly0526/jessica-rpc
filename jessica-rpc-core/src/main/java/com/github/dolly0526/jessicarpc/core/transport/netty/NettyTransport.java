package com.github.dolly0526.jessicarpc.core.transport.netty;

import com.github.dolly0526.jessicarpc.core.client.dispatcher.ResponseFuture;
import com.github.dolly0526.jessicarpc.core.client.dispatcher.ResponsePendingCenter;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * 基于netty实现的客户端通信接口
 *
 * @author yusenyang
 * @create 2021/3/8 20:28
 */
public class NettyTransport implements Transport {

    // netty的管道，每个客户端持有一个channel对象
    private final Channel channel;

    // 存放所有还没返回结果的请求，每个客户端持有一个
    private final ResponsePendingCenter responsePendingCenter;


    public NettyTransport(Channel channel, ResponsePendingCenter responsePendingCenter) {
        this.channel = channel;
        this.responsePendingCenter = responsePendingCenter;
    }


    @Override
    public CompletableFuture<Command> send(Command request) {

        // 构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();

        // 本机唯一的requestId
        int requestId = request.getHeader().getRequestId();

        // 此处处理异常必须尽量完善！
        try {
            // 将在途请求放到requestPendingCenter中
            responsePendingCenter.put(new ResponseFuture(requestId, completableFuture));

            // 发送命令并且立即写出，同时需要添加一个监听器来监控该次请求是否报错
            channel.writeAndFlush(request)
                    .addListener((ChannelFutureListener) channelFuture -> {

                        // 处理发送失败的情况
                        if (!channelFuture.isSuccess()) {
                            completableFuture.completeExceptionally(channelFuture.cause());
                            channel.close();
                        }
                    });
        } catch (Throwable t) {

            // 处理发送异常
            responsePendingCenter.remove(requestId);
            completableFuture.completeExceptionally(t);
        }

        return completableFuture;
    }
}
