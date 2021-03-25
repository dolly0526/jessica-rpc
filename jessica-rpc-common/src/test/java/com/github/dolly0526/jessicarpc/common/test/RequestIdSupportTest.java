package com.github.dolly0526.jessicarpc.common.test;

import com.github.dolly0526.jessicarpc.common.support.RequestIdSupport;
import org.junit.Test;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author yusenyang
 * @create 2021/3/25 10:58
 */
public class RequestIdSupportTest {

    @Test
    public void testLongAdder() {

        LongAdder longAdder = new LongAdder();
        System.out.println(longAdder.intValue());
        System.out.println(longAdder.intValue());

        longAdder.increment();
        System.out.println(longAdder.intValue());

        System.out.println("==============================");
        System.out.println(RequestIdSupport.next());
        System.out.println(RequestIdSupport.next());
        System.out.println(RequestIdSupport.next());
    }
}
