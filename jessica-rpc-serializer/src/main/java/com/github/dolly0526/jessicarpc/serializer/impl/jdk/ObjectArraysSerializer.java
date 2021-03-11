package com.github.dolly0526.jessicarpc.serializer.impl.jdk;

import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

/**
 * 用jdk自带序列化处理参数
 *
 * @author yusenyang
 * @create 2021/3/11 13:30
 */
public class ObjectArraysSerializer extends ObjectSerializer {

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_OBJECT_ARRAY;
    }

    @Override
    public Class getSerializeClass() {
        return Object[].class;
    }
}
