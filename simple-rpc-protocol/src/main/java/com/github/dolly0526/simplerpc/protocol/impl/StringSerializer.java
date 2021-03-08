package com.github.dolly0526.simplerpc.protocol.impl;

import com.github.dolly0526.simplerpc.protocol.Serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符串类型的序列化实现
 *
 * @author yusenyang
 * @create 2021/3/8 18:17
 */
public class StringSerializer implements Serializer<String> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    @Override
    public int size(String entry) {
        return entry.getBytes(DEFAULT_CHARSET).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = entry.getBytes(DEFAULT_CHARSET);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, DEFAULT_CHARSET);
    }

    @Override
    public byte type() {
        return Types.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
