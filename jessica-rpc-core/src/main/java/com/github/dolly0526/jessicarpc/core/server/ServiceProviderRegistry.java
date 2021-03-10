package com.github.dolly0526.jessicarpc.core.server;

/**
 * @author yusenyang
 * @create 2021/3/9 17:44
 */
public interface ServiceProviderRegistry {

    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}
