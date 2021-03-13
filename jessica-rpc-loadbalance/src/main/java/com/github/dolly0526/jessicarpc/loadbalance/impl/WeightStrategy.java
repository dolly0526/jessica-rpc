package com.github.dolly0526.jessicarpc.loadbalance.impl;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.loadbalance.RouteStrategy;

import java.net.URI;
import java.util.List;

/**
 * TODO 权重路由策略，实现doRoute方法即可
 *
 * @author yusenyang
 * @create 2021/3/13 10:22
 */
@Singleton
public class WeightStrategy implements RouteStrategy {

    @Override
    public URI doRoute(List<URI> uris) {
        return null;
    }
}
