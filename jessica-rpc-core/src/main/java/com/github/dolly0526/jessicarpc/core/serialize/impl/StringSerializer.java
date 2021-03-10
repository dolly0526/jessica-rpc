package com.github.dolly0526.jessicarpc.core.serialize.impl;

import com.github.dolly0526.jessicarpc.core.serialize.Serializer;
import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;


/**
 * 字符串类型的序列化实现
 *
 * @author yusenyang
 * @create 2021/3/8 18:17
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public int size(String entry) {
        return entry.getBytes(JessicaRpcConst.DEFAULT_CHARSET).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = entry.getBytes(JessicaRpcConst.DEFAULT_CHARSET);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, JessicaRpcConst.DEFAULT_CHARSET);
    }

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
