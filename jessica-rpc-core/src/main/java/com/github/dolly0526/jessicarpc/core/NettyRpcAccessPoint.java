package com.github.dolly0526.jessicarpc.core;

import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;
import com.github.dolly0526.jessicarpc.core.server.ServiceProviderRegistry;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.TransportClient;
import com.github.dolly0526.jessicarpc.core.transport.TransportServer;
import com.github.dolly0526.jessicarpc.api.RpcAccessPoint;
import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import com.github.dolly0526.jessicarpc.core.client.StubFactory;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author yusenyang
 * @create 2021/3/9 19:16
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {

    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server = null;
    private TransportClient client = ServiceSpiSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSpiSupport.load(StubFactory.class);
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSpiSupport.load(ServiceProviderRegistry.class);


    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {

        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);

        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server) {
            server = ServiceSpiSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);

        }
        return () -> {
            if (null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() {
        if (null != server) {
            server.stop();
        }
        client.close();
    }
}
