package com.github.dolly0526.simplerpc.core.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import com.github.dolly0526.simplerpc.core.client.stubs.RpcRequest;

import java.nio.ByteBuffer;

/**
 * @author yusenyang
 * @create 2021/3/9 19:19
 */
public class RpcRequestSerializer extends AbstractJsonSerializer<RpcRequest> {

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        // 利用fastjson序列化
        byte[] jsonBytes = JSON.toJSONString(entry).getBytes(SimpleRpcConst.DEFAULT_CHARSET);

        // 先塞一个长度进去，再放内容
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        // 第一个int是长度
        int sizeOfJson = buffer.getInt();
        byte[] jsonBytes = new byte[sizeOfJson];

        // 再获取内容
        buffer.get(jsonBytes);
        return JSON.parseObject(jsonBytes, getSerializeClass());
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
