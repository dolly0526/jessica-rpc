package com.github.dolly0526.jessicarpc.core.client;

import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * @author yusenyang
 * @create 2021/3/9 14:53
 */
public interface StubFactory {

    /**
     * 创建一个桩的实例
     *
     * @param transport
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
