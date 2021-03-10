package com.github.dolly0526.jessicarpc.core.serialize.impl.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.dolly0526.jessicarpc.core.serialize.Serializer;
import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;

/**
 * 用通用的序列化方法JSON再封装一层，同理可使用Hessian、Protobuf等通用的序列化工具进行序列化
 *
 * @author yusenyang
 * @create 2021/3/9 14:14
 */
public abstract class AbstractJsonSerializer<T> implements Serializer<T> {

    @Override
    public int size(T entry) {

        // 先放长度，再放内容
        return Integer.BYTES + JSON.toJSONString(entry).getBytes(JessicaRpcConst.DEFAULT_CHARSET).length;
    }
}
