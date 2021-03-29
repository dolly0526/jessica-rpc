package com.github.dolly0526.jessicarpc.sample.api;

import co.paralleluniverse.fibers.SuspendExecution;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloRequest;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloResult;

/**
 * @author yusenyang
 * @create 2021/3/8 16:38
 */
public interface HelloService {

    String hello(String name) throws SuspendExecution;

    HelloResult helloMoreResult(String name, Long value) throws SuspendExecution;

    HelloResult helloMoreResult(HelloRequest request) throws SuspendExecution;
}