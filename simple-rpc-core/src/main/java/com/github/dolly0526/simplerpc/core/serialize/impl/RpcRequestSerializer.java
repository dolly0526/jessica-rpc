package com.github.dolly0526.simplerpc.core.serialize.impl;

import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import com.github.dolly0526.simplerpc.core.client.stubs.RpcRequest;
import com.github.dolly0526.simplerpc.core.serialize.Serializer;

import java.nio.ByteBuffer;

/**
 * @author yusenyang
 * @create 2021/3/9 19:19
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {

    @Override
    public int size(RpcRequest request) {
        return Integer.BYTES + request.getInterfaceName().getBytes(SimpleRpcConst.DEFAULT_CHARSET).length +
                Integer.BYTES + request.getMethodName().getBytes(SimpleRpcConst.DEFAULT_CHARSET).length +
                Integer.BYTES + request.getSerializedArguments().length;
    }

    @Override
    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {

        // 采用ByteBuffer接口，依次处理 全类名、方法名、参数
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        byte[] tmpBytes = request.getInterfaceName().getBytes(SimpleRpcConst.DEFAULT_CHARSET);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = request.getMethodName().getBytes(SimpleRpcConst.DEFAULT_CHARSET);
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
        String interfaceName = new String(tmpBytes, SimpleRpcConst.DEFAULT_CHARSET);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String methodName = new String(tmpBytes, SimpleRpcConst.DEFAULT_CHARSET);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        byte[] serializedArgs = tmpBytes;

        return new RpcRequest(interfaceName, methodName, serializedArgs);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
