package com.github.dolly0526.jessicarpc.loadbalance.impl;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.loadbalance.RouteStrategy;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询负载均衡策略
 *
 * @author yusenyang
 * @create 2021/3/13 09:56
 */
@Singleton
public class RoundRobinStrategy implements RouteStrategy {
    private AtomicLong atomicLong = new AtomicLong(0);


    @Override
    public URI doRoute(List<URI> uris) {

        // 选择下标 [0, size)
        int index = (int) atomicLong.incrementAndGet() % uris.size();

        // 返回uri
        return uris.get(index);
    }
}
