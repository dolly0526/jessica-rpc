package com.github.dolly0526.simplerpc.api;

import java.io.IOException;
import java.net.URI;

/**
 * 注册中心
 *
 * @author yusenyang
 * @create 2021/3/8 16:32
 */
public interface NameService {

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param uri         服务地址
     */
    void registerService(String serviceName, URI uri) throws IOException;

    /**
     * 查询服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    URI lookupService(String serviceName) throws IOException;
}