package com.github.dolly0526.jessicarpc.nameservice;

import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.common.model.Metadata;
import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yusenyang
 * @create 2021/3/9 17:36
 */
@Slf4j
@Singleton
public class LocalFileNameService implements NameService {

    // 只支持本地文件
    private static final Collection<String> schemes = Collections.singleton("file");

    // 对应的文件对象
    private File file;


    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {

        // 连接到注册中心，本实现中只需要打开文件即可
        if (schemes.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);

        } else {
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI uri) throws IOException {
        log.info("Register service: {}, uri: {}.", serviceName, uri);

        // 基于nio读写文件系统
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {

            // 加文件锁防止并发问题
            FileLock lock = fileChannel.lock();

            // 读写文件，修改metadata对象及文件
            try {
                int fileLength = (int) raf.length();
                Metadata metadata;
                byte[] bytes;

                if (fileLength > 0) {
                    bytes = new byte[(int) raf.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);

                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }
                    metadata = SerializeSupport.parse(bytes);

                } else {
                    metadata = new Metadata();
                }

                // 添加这个服务，也即全类名
                List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
                }

                log.info(metadata.toString());
                bytes = SerializeSupport.serialize(metadata);

                // 重写文件元数据
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);

            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        Metadata metadata;

        // 基于nio读写文件系统
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {

            // 加文件锁防止并发问题
            FileLock lock = fileChannel.lock();

            // 读取文件，构造metadata对象
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);

                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }

                metadata = bytes.length == 0 ? new Metadata() : SerializeSupport.parse(bytes);
                log.info(metadata.toString());

            } finally {
                lock.release();
            }
        }

        // 从元数据中获取服务
        List<URI> uris = metadata.get(serviceName);
        if (uris == null || uris.isEmpty()) {
            return null;

        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }
}
