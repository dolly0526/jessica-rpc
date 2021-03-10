package com.github.dolly0526.jessicarpc.core.transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @author yusenyang
 * @create 2021/3/9 18:59
 */
public interface TransportClient extends Closeable {

    Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException;

    @Override
    void close();
}
