package com.github.dolly0526.simplerpc.core.transport;

/**
 * @author yusenyang
 * @create 2021/3/9 19:00
 */
public interface TransportServer {

    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();
}
