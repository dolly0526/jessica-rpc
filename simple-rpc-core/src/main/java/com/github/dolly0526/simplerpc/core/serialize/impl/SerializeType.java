package com.github.dolly0526.simplerpc.core.serialize.impl;

/**
 * 序列化类型，注意Serializer.type()是byte类型的，[-128, 127]范围内的数字可以直接转成byte，只占用一个字节
 *
 * @author yusenyang
 * @create 2021/3/8 18:19
 */
public interface SerializeType {

    int TYPE_STRING = 0;
    int TYPE_METADATA = 100;
    int TYPE_RPC_REQUEST = 101;
}
