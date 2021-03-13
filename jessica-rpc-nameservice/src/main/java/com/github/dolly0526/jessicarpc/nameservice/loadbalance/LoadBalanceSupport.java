package com.github.dolly0526.jessicarpc.nameservice.loadbalance;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

/**
 * 负载均衡工具类
 *
 * @author yusenyang
 * @create 2021/3/13 09:54
 */
@Slf4j
@Singleton
public class LoadBalanceSupport {

    /**
     * 从一堆uri中选择一个，选择方式由该策略决定
     */
    public static URI route(List<URI> uris) {

        // 参数校验
        if (uris == null || uris.isEmpty()) {
            throw new IllegalArgumentException("uris must not be null!");
        }

        // 通过spi加载路由器
        RouteStrategy routeStrategy = ServiceSpiSupport.load(RouteStrategy.class);

        // 实际执行路由
        URI routeUri = routeStrategy.doRoute(uris);

        log.info("选择路由: {}...", routeUri);
        return routeUri;
    }
}
