package com.github.dolly0526.jessicarpc.core.client;

import com.github.dolly0526.jessicarpc.core.transport.Transport;

/**
 * 生成接口代理的工厂类
 *
 * @author yusenyang
 * @create 2021/3/9 14:53
 */
public interface StubFactory {

    /**
     * 创建一个桩的实例
     */
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
