package com.github.dolly0526.jessicarpc.core.serialize.impl;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.core.client.stubs.RpcRequest;
import com.github.dolly0526.jessicarpc.core.serialize.Serializer;

import java.nio.ByteBuffer;

/**
 * 自定义的实现，用于对最终的rpc请求的序列化
 *
 * @author yusenyang
 * @create 2021/3/9 19:19
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {

    @Override
    public int size(RpcRequest request) {
        return Integer.BYTES + request.getInterfaceName().getBytes(JessicaRpcConst.DEFAULT_CHARSET).length +
                Integer.BYTES + request.getMethodName().getBytes(JessicaRpcConst.DEFAULT_CHARSET).length +
                Integer.BYTES + request.getSerializedArguments().length;
    }

    @Override
    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        byte[] tmpBytes = request.getInterfaceName().getBytes(JessicaRpcConst.DEFAULT_CHARSET);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = request.getMethodName().getBytes(JessicaRpcConst.DEFAULT_CHARSET);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = request.getSerializedArguments();
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        int len = buffer.getInt();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String interfaceName = new String(tmpBytes, JessicaRpcConst.DEFAULT_CHARSET);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String methodName = new String(tmpBytes, JessicaRpcConst.DEFAULT_CHARSET);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        byte[] serializedArgs = tmpBytes;

        return new RpcRequest(interfaceName, methodName, serializedArgs);
    }

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
