package com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.common.support.IntByteSupport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import com.github.dolly0526.jessicarpc.core.transport.protocol.ResponseHeader;

import java.io.InputStream;

/**
 * @author yusenyang
 * @create 2021/3/17 20:14
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(InputStream inputStream, byte[] integerBytes) throws Exception {

        // 构造请求头部，注意构造器入参顺序
        inputStream.read(integerBytes);
        int type = IntByteSupport.bytesToInt(integerBytes);

        inputStream.read(integerBytes);
        int version = IntByteSupport.bytesToInt(integerBytes);

        inputStream.read(integerBytes);
        int requestId = IntByteSupport.bytesToInt(integerBytes);

        inputStream.read(integerBytes);
        int code = IntByteSupport.bytesToInt(integerBytes);

        inputStream.read(integerBytes);
        int errorLength = IntByteSupport.bytesToInt(integerBytes);

        byte[] errorBytes = new byte[errorLength];
        inputStream.read(errorBytes);
        String error = new String(errorBytes, JessicaRpcConst.DEFAULT_CHARSET);

        // 返回响应头部
        return new ResponseHeader(type, version, requestId, code, error);
    }
}
