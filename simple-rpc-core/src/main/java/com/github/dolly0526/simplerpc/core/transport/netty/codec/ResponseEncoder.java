package com.github.dolly0526.simplerpc.core.transport.netty.codec;

import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import com.github.dolly0526.simplerpc.core.transport.command.Header;
import com.github.dolly0526.simplerpc.core.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yusenyang
 * @create 2021/3/9 12:08
 */
public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {

        // 先写出部分字段
        super.encodeHeader(channelHandlerContext, header, byteBuf);

        // 继续写剩下的字段
        if (header instanceof ResponseHeader) {

            // 先写出响应code
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());

            // 计算错误信息的长度
            int errorLength = responseHeader.length() - (Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                    SimpleRpcConst.DEFAULT_LENGTH_FIELD);

            // 写出错误信息的长度和内容
            byteBuf.writeInt(errorLength);
            byteBuf.writeBytes(responseHeader.getError() == null ? new byte[0]
                    : responseHeader.getError().getBytes(SimpleRpcConst.DEFAULT_CHARSET));

        } else {
            throw new Exception(String.format("Invalid header type: %s!", header.getClass().getCanonicalName()));
        }
    }
}