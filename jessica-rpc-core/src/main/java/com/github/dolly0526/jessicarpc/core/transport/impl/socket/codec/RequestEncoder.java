package com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;

import java.io.OutputStream;

/**
 * @author yusenyang
 * @create 2021/3/17 20:14
 */
public class RequestEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(OutputStream outputStream, Header header) throws Exception {

        // 将请求头部写到outputStream
        super.encodeHeader(outputStream, header);
    }
}
