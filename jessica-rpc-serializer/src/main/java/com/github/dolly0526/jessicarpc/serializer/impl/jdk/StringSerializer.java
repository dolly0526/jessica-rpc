package com.github.dolly0526.jessicarpc.serializer.impl.jdk;

import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.serializer.Serializer;
import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

/**
 * @author yusenyang
 * @create 2021/3/10 16:09
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
    public <E> String parse(byte[] bytes, int offset, int length, Class<E> eClass) {
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
