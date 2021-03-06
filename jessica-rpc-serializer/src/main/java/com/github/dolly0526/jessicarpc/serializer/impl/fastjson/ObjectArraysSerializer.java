package com.github.dolly0526.jessicarpc.serializer.impl.fastjson;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

/**
 * 用于对rpc请求所调用方法的参数进行序列化
 *
 * @author yusenyang
 * @create 2021/3/10 15:50
 */
@Singleton
public class ObjectArraysSerializer extends ObjectSerializer {

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_OBJECT_ARRAY;
    }

    // TODO 序列化时需要带上参数类型；如果跨语言，参数类型的关系怎么处理？
    @Override
    public Class getSerializeClass() {
        return Object[].class;
    }
}
