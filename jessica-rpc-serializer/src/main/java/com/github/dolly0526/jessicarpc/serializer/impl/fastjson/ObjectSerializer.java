package com.github.dolly0526.jessicarpc.serializer.impl.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

import java.nio.ByteBuffer;

/**
 * 用于对rpc请求的返回值序列化
 *
 * @author yusenyang
 * @create 2021/3/10 15:47
 */
@Singleton
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

    // TODO 基本类型、包装类、void等怎么处理？
    @Override
    public <E> Object parse(byte[] bytes, int offset, int length, Class<E> eClass) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        // 第一个int是长度
        int sizeOfJson = buffer.getInt();
        byte[] jsonBytes = new byte[sizeOfJson];

        // 再获取内容
        buffer.get(jsonBytes);

        // 此处类型推断，如果传了eClass就用这个，没有就用默认的
        if (eClass == null) {
            return JSON.parseObject(jsonBytes, getSerializeClass());

        } else {
            return JSON.parseObject(jsonBytes, eClass);
        }
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
