package com.github.dolly0526.jessicarpc.core.client.impl;

import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * @author yusenyang
 * @create 2021/3/10 20:37
 */
public class JdkStubFactory implements StubFactory {

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        return null;
    }
}
