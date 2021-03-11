package com.github.dolly0526.jessicarpc.serializer.impl.jdk;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.serializer.Serializer;
import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

import java.io.*;

/**
 * 用jdk自带的序列化方式处理
 *
 * @author yusenyang
 * @create 2021/3/11 13:23
 */
@Singleton
public class ObjectSerializer implements Serializer<Object> {

    @Override
    public int size(Object entry) {
        return toByteArray(entry).length;
    }

    @Override
    public void serialize(Object entry, byte[] bytes, int offset, int length) {

        byte[] objBytes = toByteArray(entry);
        System.arraycopy(objBytes, 0, bytes, offset, objBytes.length);
    }

    @Override
    public <E> Object parse(byte[] bytes, int offset, int length, Class<E> eClass) {

        byte[] subBytes = new byte[length];
        System.arraycopy(bytes, offset, subBytes, 0, length);
        return toObject(subBytes);
    }

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_OBJECT;
    }

    @Override
    public Class<Object> getSerializeClass() {
        return Object.class;
    }


    /**
     * 对象转字节数组
     */
    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(obj);
            oos.flush();
            bytes = baos.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 字节数组转对象
     */
    private Object toObject(byte[] bytes) {
        Object object = null;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            object = ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
