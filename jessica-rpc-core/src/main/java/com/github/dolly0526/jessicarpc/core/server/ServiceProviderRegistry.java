package com.github.dolly0526.jessicarpc.core.server;

/**
 * @author yusenyang
 * @create 2021/3/9 17:44
 */
public interface ServiceProviderRegistry {

    /**
     * 在服务端添加对外服务 <接口类名, 实现类实例>
     */
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}
