package com.github.dolly0526.jessicarpc.core.client.impl;

import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.client.stub.CglibDynamicStub;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 基于cglib，为接口生成代理
 *
 * @author yusenyang
 * @create 2021/3/10 20:31
 */
public class CglibStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {

        try {
            // 通过字节码技术动态创建子类实例
            CglibDynamicStub<T> stub = new CglibDynamicStub<>(serviceClass);
            stub.setTransport(transport);

            // 返回这个桩
            return (T) Enhancer.create(serviceClass, stub);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
