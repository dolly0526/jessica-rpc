package com.github.dolly0526.jessicarpc.core.server;

import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令注册中心单例，支持多种命令处理器，便于扩展
 *
 * @author yusenyang
 * @create 2021/3/9 17:45
 */
@Slf4j
@Singleton
public class RequestHandlerRegistry {

    // <命令类型, 命令处理器> TODO 可以扩展心跳请求等
    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    private static volatile RequestHandlerRegistry instance = null;


    // 初始化时通过spi加载所有的命令处理器
    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = ServiceSpiSupport.loadAll(RequestHandler.class);

        for (RequestHandler requestHandler : requestHandlers) {
            handlerMap.put(requestHandler.type(), requestHandler);
            log.info("Load request handler, type: {}, class: {}.", requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }


    /**
     * 获取RequestHandlerRegistry单例
     */
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

    /**
     * 根据命令类型获取对应的处理器
     */
    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }
}
