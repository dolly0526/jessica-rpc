package com.github.dolly0526.jessicarpc.core.transport.netty.codec;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 根据请求头部格式解码即可
 *
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
                byteBuf.readInt());
    }
}
