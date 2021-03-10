package com.github.dolly0526.jessicarpc.core.client.impl;

import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.client.stubs.CglibServiceStub;
import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * 基于cglib生成代理
 *
 * @author yusenyang
 * @create 2021/3/10 20:31
 */
public class CglibStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {

        try {
            CglibServiceStub<T> proxy = new CglibServiceStub<>(serviceClass);
            proxy.setTransport(transport);

            // 返回这个桩
            return (T) proxy.getProxy();

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
