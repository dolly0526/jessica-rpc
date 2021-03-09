package com.github.dolly0526.simplerpc.core.transport.netty.codec;

import com.github.dolly0526.simplerpc.core.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yusenyang
 * @create 2021/3/9 12:06
 */
public class RequestEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {

        // 将请求头部写到byteBuf缓冲区
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }
}
