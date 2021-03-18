package com.github.dolly0526.jessicarpc.core;

import com.github.dolly0526.jessicarpc.api.RpcAccessPoint;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.server.ServiceProviderRegistry;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.TransportClient;
import com.github.dolly0526.jessicarpc.core.transport.TransportServer;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于spi封装和实现的一组服务端和客户端的api
 *
 * @author yusenyang
 * @create 2021/3/9 19:16
 */
@Singleton
public class SpiRpcAccessPoint implements RpcAccessPoint {

    // TODO 目前仅支持本地的rpc
    private final String host = "localhost";
    private final int port = 8070;
    private final URI uri = URI.create("rpc://" + host + ":" + port);

    private TransportServer server = null;
    private TransportClient client = ServiceSpiSupport.load(TransportClient.class);

    // 存放多个通信对象，对每个uri持有一个
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    // 生成接口代理的工厂类
    private final StubFactory stubFactory = ServiceSpiSupport.load(StubFactory.class);

    // 服务注册中心
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSpiSupport.load(ServiceProviderRegistry.class);


    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {

        // 启动客户端中的通信对象，不能重复创建
        Transport transport = clientMap.computeIfAbsent(uri, key -> {
            try {
                return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return stubFactory.createStub(transport, serviceClass);
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {

        // 将该服务注册到注册中心
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {

        // 通过spi启动
        if (server == null) {
            server = ServiceSpiSupport.load(TransportServer.class);
            server.start(port);
        }
        return server;
    }

    @Override
    public void close() {
        if (server != null) {
            server.close();
        }
        client.close();
    }
}
