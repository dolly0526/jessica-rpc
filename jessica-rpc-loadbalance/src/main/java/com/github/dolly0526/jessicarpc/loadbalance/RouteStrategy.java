package com.github.dolly0526.jessicarpc.loadbalance;

import java.net.URI;
import java.util.List;

/**
 * 负载均衡抽象类
 *
 * @author yusenyang
 * @create 2021/3/13 09:49
 */
public interface RouteStrategy {

    /**
     * 从一堆uri中选择一个
     *
     * @param uris 可以选择的uri集合
     * @return 选择的uri
     */
    URI doRoute(List<URI> uris);
}
