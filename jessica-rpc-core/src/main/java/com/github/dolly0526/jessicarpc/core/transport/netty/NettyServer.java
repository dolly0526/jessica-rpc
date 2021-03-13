package com.github.dolly0526.jessicarpc.core.transport.netty;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;
import com.github.dolly0526.jessicarpc.core.transport.TransportServer;
import com.github.dolly0526.jessicarpc.core.transport.netty.codec.RequestDecoder;
import com.github.dolly0526.jessicarpc.core.transport.netty.codec.ResponseEncoder;
import com.github.dolly0526.jessicarpc.core.transport.netty.handler.RequestInvocation;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于netty实现服务端
 *
 * @author yusenyang
 * @create 2021/3/9 19:02
 */
@Slf4j
@Singleton
public class NettyServer implements TransportServer {

    // 管理所有命令处理器
    private RequestHandlerRegistry requestHandlerRegistry;

    // netty客户端必备的一些对象
    private EventLoopGroup acceptEventGroup;
    private EventLoopGroup ioEventGroup;
    private Channel channel;


    // spi加载的时候调用无参构造器，初始化requestHandlerRegistry
    public NettyServer() {
        this.requestHandlerRegistry = RequestHandlerRegistry.getInstance();
    }


    @Override
    public void start(int port) throws Exception {

        // 主从reactor需要两个group
        acceptEventGroup = newEventLoopGroup();
        ioEventGroup = newEventLoopGroup();

        // 初始化服务端
        ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
        ServerBootstrap serverBootstrap = newBootstrap(channelHandlerPipeline, acceptEventGroup, ioEventGroup);

        // 绑定端口
        channel = doBind(serverBootstrap, port);
    }

    /**
     * 绑定端口
     */
    private Channel doBind(ServerBootstrap serverBootstrap, int port) throws Exception {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }

    /**
     * 初始化工作group，优先使用epoll
     */
    private EventLoopGroup newEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    /**
     * 初始化pipeline，一对编解码handler，以及一个业务处理handler；此处已经处理了粘包半包
     */
    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new RequestDecoder())
                        .addLast(new ResponseEncoder())
                        .addLast(new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    /**
     * 初始化服务端，优先使用epoll
     */
    private ServerBootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup acceptEventGroup, EventLoopGroup ioEventGroup) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 优先使用epoll，根据当前的操作系统选择是否开启
        serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .group(acceptEventGroup, ioEventGroup)
                .childHandler(channelHandler)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        return serverBootstrap;
    }

    @Override
    public void close() {
        if (acceptEventGroup != null) {
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }
}
