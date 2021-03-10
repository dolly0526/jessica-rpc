package com.github.dolly0526.jessicarpc.core.client;

import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * @author yusenyang
 * @create 2021/3/9 15:12
 */
public interface ServiceStub {

    /**
     * 赋予传输对象用于通信
     *
     * @param transport
     */
    void setTransport(Transport transport);
}
