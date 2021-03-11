package com.github.dolly0526.jessicarpc.sample.api;

import com.github.dolly0526.jessicarpc.sample.api.model.HelloRequest;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloResult;

/**
 * @author yusenyang
 * @create 2021/3/8 16:38
 */
public interface HelloService {

    String hello(String name);

    HelloResult helloMoreResult(String name, Long value);

    HelloResult helloMoreResult(HelloRequest request);
}