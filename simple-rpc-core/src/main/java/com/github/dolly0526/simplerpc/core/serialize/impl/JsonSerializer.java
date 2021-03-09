package com.github.dolly0526.simplerpc.core.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import com.github.dolly0526.simplerpc.core.serialize.Serializer;

/**
 * @author yusenyang
 * @create 2021/3/9 14:14
 */
public abstract class JsonSerializer<T> implements Serializer<T> {

    @Override
    public int size(T entry) {
        return Integer.BYTES + JSON.toJSONString(entry).getBytes(SimpleRpcConst.DEFAULT_CHARSET).length;
    }
}
