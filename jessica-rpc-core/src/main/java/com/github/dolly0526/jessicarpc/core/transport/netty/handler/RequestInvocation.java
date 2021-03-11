package com.github.dolly0526.jessicarpc.core.transport.netty.handler;

import com.github.dolly0526.jessicarpc.core.server.RequestHandler;
import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yusenyang
 * @create 2021/3/9 12:16
 */
@Slf4j
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {

    // 存放所有在途请求，初始化时传入
    private final RequestHandlerRegistry requestHandlerRegistry;


    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command request) throws Exception {

        // 此时请求已完成解码，根据type从命令注册中心获取对应的handler
        RequestHandler handler = requestHandlerRegistry.get(request.getHeader().getType());
        if (handler != null) {

            // 用handler处理请求
            Command response = handler.handle(request);
            if (response != null) {

                channelHandlerContext.writeAndFlush(response)
                        .addListener((ChannelFutureListener) channelFuture -> {

                            // 注意处理发送失败的情况
                            if (!channelFuture.isSuccess()) {
                                log.warn("Write response failed!", channelFuture.cause());
                                channelHandlerContext.channel().close();
                            }
                        });
            } else {
                log.warn("Response is null!");
            }
        } else {
            throw new Exception(String.format("No handler for request with type: %d!", request.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        log.warn("Exception: ", cause);
        super.exceptionCaught(ctx, cause);

        Channel channel = ctx.channel();
        if (channel.isActive()) ctx.close();
    }
}
