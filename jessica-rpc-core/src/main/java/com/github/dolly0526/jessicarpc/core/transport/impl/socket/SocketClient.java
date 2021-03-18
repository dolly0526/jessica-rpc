package com.github.dolly0526.jessicarpc.core.transport.impl.socket;

import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * @author yusenyang
 * @create 2021/3/17 18:53
 */
@Slf4j
public class SocketClient implements TransportClient {

    @Override
    public Transport createTransport(SocketAddress address, int connectionTimeout) throws Exception {

        // 初始化transport对象
        return new SocketTransport(address, connectionTimeout);
    }

    @Override
    public void close() {
    }
}
