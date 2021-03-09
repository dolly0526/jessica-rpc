package com.github.dolly0526.simplerpc.core.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于生成请求id
 *
 * @author yusenyang
 * @create 2021/3/9 15:13
 */
public class RequestIdSupport {

    private final static AtomicInteger nextRequestId = new AtomicInteger(0);

    public static int next() {
        return nextRequestId.getAndIncrement();
    }
}
