package com.github.dolly0526.jessicarpc.core.server;

import com.github.dolly0526.jessicarpc.api.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yusenyang
 * @create 2021/3/9 17:45
 */
@Slf4j
public class RequestHandlerRegistry {

    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    private static volatile RequestHandlerRegistry instance = null;


    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);

        for (RequestHandler requestHandler : requestHandlers) {
            handlerMap.put(requestHandler.type(), requestHandler);
            log.info("Load request handler, type: {}, class: {}.", requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }


    public static RequestHandlerRegistry getInstance() {

        // DCL单例，保证线程安全
        if (instance == null) {
            synchronized (RequestHandlerRegistry.class) {
                if (instance == null) {
                    instance = new RequestHandlerRegistry();
                }
            }
        }
        return instance;
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }
}
