package com.github.dolly0526.jessicarpc.core.transport.netty.codec;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yusenyang
 * @create 2021/3/9 11:45
 */
public abstract class CommandDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        // 此处解决半包问题，如果连存放长度的4字节都被拆开了，则继续等待
        if (!byteBuf.isReadable(JessicaRpcConst.DEFAULT_LENGTH_FIELD)) {
            return;
        }

        // 对当前byteBuf的index进行存档，处理粘包半包问题
        byteBuf.markReaderIndex();

        // 此处先读出一个int，此为消息长度，注意需要长度减去固定的4
        int length = byteBuf.readInt() - JessicaRpcConst.DEFAULT_LENGTH_FIELD;

        // 如果当前长度不满足一个包，则先重置索引后返回，后续长度满足了再处理
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        // 解码消息头部
        Header header = decodeHeader(channelHandlerContext, byteBuf);

        // 读取完头部，计算消息内容的长度
        int payloadLength = length - header.length();

        // 从bytebuf中获取消息内容
        byte[] payload = new byte[payloadLength];
        byteBuf.readBytes(payload);

        // 继续在pipeline中处理
        list.add(new Command(header, payload));
    }

    /**
     * 从byteBuf读出消息头部
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @return
     */
    protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);
}
