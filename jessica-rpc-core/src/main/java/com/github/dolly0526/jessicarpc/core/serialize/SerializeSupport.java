package com.github.dolly0526.jessicarpc.core.serialize;

import com.github.dolly0526.jessicarpc.api.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 可扩展的、通用的序列化接口，对Serializer处理过的对象进一步序列化；利用SPI机制实现轻量的IOC
 *
 * @author yusenyang
 * @create 2021/3/8 18:14
 */
@Slf4j
public class SerializeSupport {

    private static Map<Class<?>/*序列化对象类型*/, Serializer<?>/*序列化实现*/> serializerMap = new HashMap<>();
    private static Map<Byte/*序列化实现类型*/, Class<?>/*序列化对象类型*/> typeMap = new HashMap<>();


    /**
     * 类加载时初始化两个map，实现ioc
     */
    static {
        for (Serializer serializer : ServiceSupport.loadAll(Serializer.class)) {

            byte serializeType = serializer.getSerializeType();
            Class serializeClass = serializer.getSerializeClass();

            serializerMap.put(serializeClass, serializer);
            typeMap.put(serializeType, serializeClass);

            log.info("Found serializer, class: {}, type: {}.",
                    serializeClass.getCanonicalName(), serializeType);
        }
    }


    /**
     * 拿到一个byte流，直接从0处理到末尾
     */
    public static <E> E parse(byte[] bytes) {
        return parse(bytes, 0, bytes.length);
    }

    /**
     * 处理该byte流，提取首位获取类型，和typeMap交互
     */
    @SuppressWarnings("unchecked")
    private static <E> E parse(byte[] bytes, int offset, int length) {

        // 从byte流的首位获取类型
        byte type = bytes[0];

        // 从map中根据类型获取反序列化之后的类型
        Class<E> eClass = (Class<E>) typeMap.get(type);

        // 此处注意偏移量和长度，要把type头摘掉，对应的长度减1
        if (eClass != null) {
            return parse(bytes, offset + 1, length - 1, eClass);

        } else {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        }
    }

    /**
     * 已经把type头摘掉之后，和serializerMap交互，进行反序列化
     */
    @SuppressWarnings("unchecked")
    private static <E> E parse(byte[] buffer, int offset, int length, Class<E> eClass) {

        // 利用SPI，根据类型获取Serializer对象，调用Serializer的parse方法反序列化，默认使用Object的序列化
        Object entry = serializerMap.getOrDefault(eClass, serializerMap.get(Object.class)).parse(buffer, offset, length);

        // 返回前还需注意判断类型是否合理
        if (eClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;

        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    /**
     * 根据类型获取对应的Serializer进行序列化，并在首位放type字段
     */
    @SuppressWarnings("unchecked")
    public static <E> byte[] serialize(E entry) {
        if (entry == null) {
            return null;
        }

        // 根据类型获取对应的Serializer，默认使用Object的序列化
        Serializer<E> serializer = (Serializer<E>) serializerMap.getOrDefault(entry.getClass(), serializerMap.get(Object.class));

        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }

        // 进一步封装，byte流的首位要类型，范围[-127, 128]
        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.getSerializeType();

        // 调用该类型的serialize方法进行序列化
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }
}
