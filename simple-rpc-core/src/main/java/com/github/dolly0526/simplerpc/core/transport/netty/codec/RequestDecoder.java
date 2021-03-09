package com.github.dolly0526.simplerpc.core.transport.netty.codec;

import com.github.dolly0526.simplerpc.core.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yusenyang
 * @create 2021/3/9 12:07
 */
public class RequestDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

        // 构造请求头部，注意构造器入参顺序
        return new Header(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt()
        );
    }
}
