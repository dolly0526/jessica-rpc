package com.github.dolly0526.jessicarpc.sample.api.impl;

import com.github.dolly0526.jessicarpc.sample.api.model.HelloRequest;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloResult;
import com.github.dolly0526.jessicarpc.sample.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yusenyang
 * @create 2021/3/8 17:04
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        log.info("HelloServiceImpl收到: {}.", name);
        String ret = "Hello, " + name;
        log.info("HelloServiceImpl返回: {}.", ret);
        return ret;
    }

    @Override
    public HelloResult helloMoreResult(String name, Long value) {

        log.info("HelloServiceImpl收到: {}, {}.", name, value);
        HelloResult result = new HelloResult();
        result.setSuccess(true);
        result.setResult("Success");
        result.setResultCode("1");
        log.info("HelloServiceImpl返回: {}.", result.toString());

        return result;
    }

    @Override
    public HelloResult helloMoreResult(HelloRequest request) {

        log.info("HelloServiceImpl收到: {}.", request.toString());
        HelloResult result = new HelloResult();
        result.setSuccess(true);
        result.setResult("Success");
        result.setResultCode("-1");
        log.info("HelloServiceImpl返回: {}.", result.toString());
        return result;
    }
}