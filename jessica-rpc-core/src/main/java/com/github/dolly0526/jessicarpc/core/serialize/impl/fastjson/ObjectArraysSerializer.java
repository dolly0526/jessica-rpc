package com.github.dolly0526.jessicarpc.core.serialize.impl.fastjson;

import com.github.dolly0526.jessicarpc.core.serialize.impl.SerializeType;

/**
 * 用于对rpc请求所调用方法的参数进行序列化
 *
 * @author yusenyang
 * @create 2021/3/10 15:50
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
