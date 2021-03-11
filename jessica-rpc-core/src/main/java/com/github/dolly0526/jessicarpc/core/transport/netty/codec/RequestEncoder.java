package com.github.dolly0526.jessicarpc.core.transport.netty.codec;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 特殊编码请求头部
 *
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
