package com.github.dolly0526.jessicarpc.core.client.impl;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.client.stub.JdkDynamicStub;
import com.github.dolly0526.jessicarpc.core.transport.Transport;

import java.lang.reflect.Proxy;

/**
 * 基于jdk，为接口生成代理
 *
 * @author yusenyang
 * @create 2021/3/10 20:37
 */
@Singleton
public class JdkStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {

        try {
            // 通过jdk原生代理动态创建子类实例
            JdkDynamicStub<T> stub = new JdkDynamicStub<>(serviceClass);
            stub.setTransport(transport);

            // 返回这个桩
            return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, stub);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
