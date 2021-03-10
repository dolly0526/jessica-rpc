package com.github.dolly0526.jessicarpc.core.client;

import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * 桩的接口，用来代理具体的实现类
 *
 * @author yusenyang
 * @create 2021/3/9 15:12
 */
public interface ServiceStub {

    /**
     * 赋予这个桩传输对象用于通信
     */
    void setTransport(Transport transport);
}
