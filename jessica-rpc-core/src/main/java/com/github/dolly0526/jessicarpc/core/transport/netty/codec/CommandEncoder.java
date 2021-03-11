package com.github.dolly0526.jessicarpc.core.transport.netty.codec;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 通用的消息编码器，和自定义的协议绑定
 *
 * @author yusenyang
 * @create 2021/3/9 12:05
 */
public abstract class CommandEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

        if (!(o instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s!", o.getClass().getCanonicalName()));
        }

        // 校验类型通过，继续处理
        Command command = (Command) o;

        // 在消息头部写出长度，多加一个4后续要减去，防止消息长度为0
        byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);

        // 写出请求头部
        encodeHeader(channelHandlerContext, command.getHeader(), byteBuf);

        // 写出请求内容
        byteBuf.writeBytes(command.getPayload());
    }

    /**
     * 将消息头部写到byteBuf缓冲区
     */
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {

        // 注意写到缓冲区的顺序，会影响读取时的顺序
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
