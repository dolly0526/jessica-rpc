package com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.common.support.IntByteSupport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;

import java.io.OutputStream;

/**
 * @author yusenyang
 * @create 2021/3/17 19:44
 */
public abstract class CommandEncoder {

    public void encode(OutputStream outputStream, Command command) throws Exception {

        // 在消息头部写出长度，多加一个4后续要减去，防止消息长度为0
        int total = JessicaRpcConst.DEFAULT_LENGTH_FIELD + command.getHeader().length() + command.getPayload().length;
        outputStream.write(IntByteSupport.intToBytes(total));

        // 写出请求头部
        encodeHeader(outputStream, command.getHeader());

        // 写出payload内容
        outputStream.write(command.getPayload());

        // 立刻写出
        outputStream.flush();
    }

    /**
     * 将header写到outputstream
     */
    protected void encodeHeader(OutputStream outputStream, Header header) throws Exception {

        // 注意写到缓冲区的顺序，会影响读取时的顺序；此处注意必须将int型手动序列化成4字节的byte
        outputStream.write(IntByteSupport.intToBytes(header.getType()));
        outputStream.write(IntByteSupport.intToBytes(header.getVersion()));
        outputStream.write(IntByteSupport.intToBytes(header.getRequestId()));
    }
}
