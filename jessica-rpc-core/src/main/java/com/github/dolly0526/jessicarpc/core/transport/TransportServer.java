package com.github.dolly0526.jessicarpc.core.transport;

import java.io.Closeable;

/**
 * @author yusenyang
 * @create 2021/3/9 19:00
 */
public interface TransportServer extends Closeable {

    /**
     * 启动一个服务端通信对象
     */
    void start(int port) throws Exception;

    @Override
    void close();
}
