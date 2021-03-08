package com.github.dolly0526.simplerpc.sample;

/**
 * @author yusenyang
 * @create 2021/3/8 17:04
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {

        String ret = "Hello, " + name;
        return ret;
    }
}