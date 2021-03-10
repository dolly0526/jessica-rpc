package com.github.dolly0526.jessicarpc.core;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author yusenyang
 * @create 2021/3/10 12:54
 */
public class TestDemo {

    @Test
    public void testByte() {

        byte[] bytes = new byte[1];
        bytes[0] = getInt();

        System.out.println(Arrays.toString(bytes));
    }

    private byte getInt() {
        return 0;
    }
}
