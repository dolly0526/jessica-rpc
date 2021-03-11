package com.github.dolly0526.jessicarpc.serializer.impl.jdk;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.common.constant.JessicaRpcConst;
import com.github.dolly0526.jessicarpc.serializer.Serializer;
import com.github.dolly0526.jessicarpc.common.model.Metadata;
import com.github.dolly0526.jessicarpc.serializer.impl.SerializeType;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Size of the map                     2 bytes
 *      Map entry:
 *          Key string:
 *              Length:                2 bytes
 *              Serialized key bytes:  variable length
 *          Value list
 *              List size:              2 bytes
 *              item(URI):
 *                  Length:             2 bytes
 *                  serialized uri:     variable length
 *              item(URI):
 *              ...
 *      Map entry:
 *      ...
 *
 * @author yusenyang
 * @create 2021/3/9 11:11
 */
@Singleton
public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return Short.BYTES +                   // Size of the map                  2 bytes
                entry.entrySet().stream()
                        .mapToInt(this::entrySize).sum();
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {

        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putShort(toShortSafely(entry.size()));

        entry.forEach((k, v) -> {
            byte[] keyBytes = k.getBytes(JessicaRpcConst.DEFAULT_CHARSET);
            buffer.putShort(toShortSafely(keyBytes.length));
            buffer.put(keyBytes);

            buffer.putShort(toShortSafely(v.size()));
            for (URI uri : v) {
                byte[] uriBytes = uri.toASCIIString().getBytes(JessicaRpcConst.DEFAULT_CHARSET);
                buffer.putShort(toShortSafely(uriBytes.length));
                buffer.put(uriBytes);
            }

        });
    }

    private int entrySize(Map.Entry<String, List<URI>> e) {
        // Map entry:
        return Short.BYTES +       // Key string length:               2 bytes
                e.getKey().getBytes().length +    // Serialized key bytes:   variable length
                Short.BYTES + // List size:              2 bytes
                e.getValue().stream() // Value list
                        .mapToInt(uri -> {
                            return Short.BYTES +       // Key string length:               2 bytes
                                    uri.toASCIIString().getBytes(JessicaRpcConst.DEFAULT_CHARSET).length;    // Serialized key bytes:   variable length
                        }).sum();
    }

    @Override
    public <E> Metadata parse(byte[] bytes, int offset, int length, Class<E> eClass) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        Metadata metadata = new Metadata();
        int sizeOfMap = buffer.getShort();
        for (int i = 0; i < sizeOfMap; i++) {
            int keyLength = buffer.getShort();
            byte[] keyBytes = new byte[keyLength];
            buffer.get(keyBytes);
            String key = new String(keyBytes, JessicaRpcConst.DEFAULT_CHARSET);


            int uriListSize = buffer.getShort();
            List<URI> uriList = new ArrayList<>(uriListSize);
            for (int j = 0; j < uriListSize; j++) {
                int uriLength = buffer.getShort();
                byte[] uriBytes = new byte[uriLength];
                buffer.get(uriBytes);
                URI uri = URI.create(new String(uriBytes, JessicaRpcConst.DEFAULT_CHARSET));
                uriList.add(uri);
            }
            metadata.put(key, uriList);
        }
        return metadata;
    }

    @Override
    public byte getSerializeType() {
        return SerializeType.TYPE_METADATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }

    private short toShortSafely(int v) {
        assert v < Short.MAX_VALUE;
        return (short) v;
    }
}
