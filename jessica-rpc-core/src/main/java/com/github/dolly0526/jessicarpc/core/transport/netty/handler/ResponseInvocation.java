package com.github.dolly0526.jessicarpc.core.transport.netty.handler;

import com.github.dolly0526.jessicarpc.core.transport.netty.dispatcher.RequestPool;
import com.github.dolly0526.jessicarpc.core.transport.netty.dispatcher.ResponseFuture;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yusenyang
 * @create 2021/3/9 12:17
 */
@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
    private final RequestPool requestPool;


    public ResponseInvocation(RequestPool requestPool) {
        this.requestPool = requestPool;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command response) {
        ResponseFuture future = requestPool.remove(response.getHeader().getRequestId());
        if (null != future) {
            future.getFuture().complete(response);
        } else {
            logger.warn("Drop response: {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception: ", cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) ctx.close();
    }
}
