package com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.common.support.IntByteSupport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import com.github.dolly0526.jessicarpc.core.transport.protocol.ResponseHeader;

import java.io.OutputStream;

/**
 * @author yusenyang
 * @create 2021/3/17 20:15
 */
public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(OutputStream outputStream, Header header) throws Exception {

        // 先写出部分字段，再继续写剩下的字段
        super.encodeHeader(outputStream, header);

        // 先写出响应code
        ResponseHeader responseHeader = (ResponseHeader) header;
        outputStream.write(IntByteSupport.intToBytes(responseHeader.getCode()));

        // 计算错误信息的长度
        int errorLength = responseHeader.length() - (Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                JessicaRpcConst.DEFAULT_LENGTH_FIELD);

        // 写出错误信息的长度和内容
        outputStream.write(IntByteSupport.intToBytes(errorLength));
        outputStream.write(responseHeader.getError() == null ? new byte[0]
                : responseHeader.getError().getBytes(JessicaRpcConst.DEFAULT_CHARSET));
    }
}
