package com.github.dolly0526.jessicarpc.api;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心
 *
 * @author yusenyang
 * @create 2021/3/8 16:32
 */
public interface NameService {

    /**
     * 所有支持的协议
     */
    Collection<String> supportedSchemes();

    /**
     * 连接注册中心
     *
     * @param nameServiceUri 注册中心地址
     */
    void connect(URI nameServiceUri);

    /**
     * 服务端方法，注册服务
     *
     * @param serviceName 服务名称，全类名
     * @param uri         服务地址
     */
    void registerService(String serviceName, URI uri) throws IOException;

    /**
     * 客户端方法，查询服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    URI lookupService(String serviceName) throws IOException;
}