package com.github.dolly0526.simplerpc.registry;

import java.net.URI;

/**
 * RPC框架对外提供的服务接口
 *
 * @author yusenyang
 * @create 2021/3/8 16:32
 */
public interface RpcAccessPoint {

    /**
     * 客户端获取远程服务的引用
     *
     * @param uri          远程服务地址
     * @param serviceClass 服务的接口类的Class
     * @param <T>          服务接口的类型
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    /**
     * 服务端注册服务的实现实例
     *
     * @param service      实现实例
     * @param serviceClass 服务的接口类的Class
     * @param <T>          服务接口的类型
     * @return 服务地址
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);
}