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
        RequestHandler handler = requestHandlerRegistry.get(request.getHeader().getType());
        if (null != handler) {
            Command response = handler.handle(request);
            if (null != response) {
                channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
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
