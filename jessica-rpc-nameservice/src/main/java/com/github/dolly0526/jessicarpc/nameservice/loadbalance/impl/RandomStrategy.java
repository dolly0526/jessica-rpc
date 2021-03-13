package com.github.dolly0526.jessicarpc.nameservice.loadbalance.impl;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.nameservice.loadbalance.RouteStrategy;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡策略
 *
 * @author yusenyang
 * @create 2021/3/13 09:56
 */
@Singleton
public class RandomStrategy implements RouteStrategy {

    @Override
    public URI doRoute(List<URI> uris) {

        // 获取[0, size)的随机数
        int index = ThreadLocalRandom.current().nextInt(uris.size());

        // 返回uri
        return uris.get(index);
    }
}
