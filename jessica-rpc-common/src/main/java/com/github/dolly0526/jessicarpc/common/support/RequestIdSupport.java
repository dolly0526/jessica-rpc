package com.github.dolly0526.jessicarpc.common.support;

import java.util.concurrent.atomic.LongAdder;

/**
 * 用于生成请求id
 *
 * @author yusenyang
 * @create 2021/3/9 15:13
 */
public class RequestIdSupport {

    // 比AtomicLong性能更好
    private final static LongAdder nextRequestId = new LongAdder();


    // TODO 换成long型返回值，否则最大值太小
    public static int next() {
        try {
            return nextRequestId.intValue();

        } finally {
            nextRequestId.increment();
        }
    }
}
