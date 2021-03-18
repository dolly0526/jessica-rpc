package com.github.dolly0526.jessicarpc.core.transport.impl.netty;

import com.github.dolly0526.jessicarpc.core.client.dispatcher.ResponsePendingCenter;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.TransportClient;
import com.github.dolly0526.jessicarpc.core.transport.impl.netty.handler.ResponseInvocation;
import com.github.dolly0526.jessicarpc.core.transport.impl.netty.codec.RequestEncoder;
import com.github.dolly0526.jessicarpc.core.transport.impl.netty.codec.ResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 基于netty实现客户端对象
 *
 * @author yusenyang
 * @create 2021/3/9 19:03
 */
public class NettyClient implements TransportClient {

    // 存放所有在途的请求，根据requestId将异步的请求对应上
    private final ResponsePendingCenter responsePendingCenter;

    // netty客户端必备的一些对象
    private EventLoopGroup ioEventGroup;
    private Bootstrap bootstrap;
    private List<Channel> channels = new LinkedList<>();


    // spi加载的时候调用无参构造器，初始化responsePendingCenter
    public NettyClient() {
        responsePendingCenter = new ResponsePendingCenter();
    }


    @Override
    public Transport createTransport(SocketAddress address, int connectionTimeout) throws InterruptedException, TimeoutException {

        // 创建一个通道，超时抛出异常
        Channel channel = createChannel(address, connectionTimeout);

        // 进而创建一个基于netty的通信对象
        return new NettyTransport(channel, responsePendingCenter);
    }

    /**
     * 创建一个通道，每一步都要注意抛异常！
     */
    private synchronized Channel createChannel(SocketAddress address, int connectionTimeout) throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null!");
        }

        // 构造工作group
        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }

        // 初始化客户端
        if (bootstrap == null) {
            ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandlerPipeline, ioEventGroup);
        }

        // 连接远端地址，超时则抛出异常
        ChannelFuture channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException();
        }

        // 获取通道，失败则抛出异常
        Channel channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }

        // 可能有多个通道，管理起来最后一起关闭
        channels.add(channel);
        return channel;
    }

    /**
     * 初始化pipeline，一对编解码handler，以及一个业务处理handler；此处已经处理了粘包半包
     */
    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        // 客户端开启5s写出的idle检测
                        .addLast(new IdleStateHandler(0, 5, 90, TimeUnit.SECONDS))
                        .addLast(new ResponseDecoder())
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseInvocation(responsePendingCenter));
            }
        };
    }

    /**
     * 初始化工作group，优先使用epoll
     */
    private EventLoopGroup newIoEventGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    /**
     * 初始化客户端，优先使用epoll
     */
    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup ioEventGroup) {
        Bootstrap bootstrap = new Bootstrap();

        // 优先使用epoll，根据当前的操作系统选择是否开启
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(channelHandler)
                // ByteBufAllocator.DEFAULT：大多池化、堆外
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        return bootstrap;
    }

    @Override
    public void close() {
        for (Channel channel : channels) {
            if (channel != null) {
                channel.close();
            }
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        responsePendingCenter.close();
    }
}
