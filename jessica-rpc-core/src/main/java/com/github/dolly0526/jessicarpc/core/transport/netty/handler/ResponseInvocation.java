package com.github.dolly0526.jessicarpc.core.transport.netty.handler;

import com.github.dolly0526.jessicarpc.core.client.dispatcher.ResponsePendingCenter;
import com.github.dolly0526.jessicarpc.core.client.dispatcher.ResponseFuture;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty客户端的handler，用来接收请求，并放进requestPendingCenter
 *
 * @author yusenyang
 * @create 2021/3/9 12:17
 */
@Slf4j
@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {

    // 存放所有在途请求，初始化时传入
    private final ResponsePendingCenter responsePendingCenter;


    public ResponseInvocation(ResponsePendingCenter responsePendingCenter) {
        this.responsePendingCenter = responsePendingCenter;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command response) {

        // 此时响应发回了，从requestPendingCenter中删掉对应的对象，把结果塞给该future对象供业务使用
        ResponseFuture future = responsePendingCenter.remove(response.getHeader().getRequestId());

        if (future != null) {
            future.getFuture().complete(response);

        } else {
            log.warn("Drop response: {}", response);
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
