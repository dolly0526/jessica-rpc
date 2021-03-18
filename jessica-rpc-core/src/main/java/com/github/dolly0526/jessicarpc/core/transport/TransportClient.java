package com.github.dolly0526.jessicarpc.core.transport;

import java.io.Closeable;
import java.net.SocketAddress;

/**
 * 客户端接口
 *
 * @author yusenyang
 * @create 2021/3/9 18:59
 */
public interface TransportClient extends Closeable {

    /**
     * 根据地址和超时时间创建一个连接对象
     */
    Transport createTransport(SocketAddress address, int connectionTimeout) throws Exception;

    @Override
    void close();
}
