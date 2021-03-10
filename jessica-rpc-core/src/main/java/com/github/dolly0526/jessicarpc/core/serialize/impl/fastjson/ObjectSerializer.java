package com.github.dolly0526.jessicarpc.core.serialize.impl.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.core.client.stubs.RpcRequest;
import com.github.dolly0526.jessicarpc.core.serialize.impl.SerializeType;

import java.nio.ByteBuffer;

/**
 * 用于对rpc请求的返回值序列化
 *
 * @author yusenyang
 * @create 2021/3/10 15:47
 */
public class ObjectSerializer extends AbstractJsonSerializer<Object> {

    @Override
    public void serialize(Object entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        // 利用fastjson序列化
        byte[] jsonBytes = JSON.toJSONString(entry).getBytes(JessicaRpcConst.DEFAULT_CHARSET);

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
        return SerializeType.TYPE_OBJECT;
    }

    @Override
    public Class<Object> getSerializeClass() {
        return Object.class;
    }
}
