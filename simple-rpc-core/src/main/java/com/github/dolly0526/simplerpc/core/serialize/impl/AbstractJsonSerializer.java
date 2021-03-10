package com.github.dolly0526.simplerpc.core.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import com.github.dolly0526.simplerpc.core.serialize.Serializer;

/**
 * 用通用的序列化方法JSON再封装一层，用于简化对参数的序列化
 *
 * @author yusenyang
 * @create 2021/3/9 14:14
 */
public abstract class AbstractJsonSerializer<T> implements Serializer<T> {

    @Override
    public int size(T entry) {

        // 先放长度，再放内容
        return Integer.BYTES + JSON.toJSONString(entry).getBytes(SimpleRpcConst.DEFAULT_CHARSET).length;
    }
}
