package com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.common.support.IntByteSupport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;

import java.io.InputStream;

/**
 * @author yusenyang
 * @create 2021/3/17 19:42
 */
public abstract class CommandDecoder {

    public Command decode(InputStream inputStream) throws Exception {

        // TODO 解决半包拆包问题
        byte[] integerBytes = new byte[Integer.BYTES];

        // 此处先读出一个int，为消息长度，注意需要长度减去固定的4
        inputStream.read(integerBytes);
        int length = IntByteSupport.bytesToInt(integerBytes) - JessicaRpcConst.DEFAULT_LENGTH_FIELD;

        // 解码消息头部，根据请求和响应分别解出不同的头部
        Header header = decodeHeader(inputStream, integerBytes);

        // 读取完头部，计算消息内容的长度
        int payloadLength = length - header.length();

        // 从inputStream中获取消息内容
        byte[] payload = new byte[payloadLength];
        inputStream.read(payload);

        // 返回command对象
        return new Command(header, payload);
    }

    /**
     * 从inputStream中读出header
     */
    protected abstract Header decodeHeader(InputStream inputStream, byte[] integerBytes) throws Exception;
}
