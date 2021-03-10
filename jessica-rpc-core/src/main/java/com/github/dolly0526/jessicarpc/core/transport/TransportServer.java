package com.github.dolly0526.jessicarpc.core.transport;

import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;

/**
 * @author yusenyang
 * @create 2021/3/9 19:00
 */
public interface TransportServer {

    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();
}
