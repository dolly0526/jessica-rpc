package com.github.dolly0526.jessicarpc.sample;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableSpringUtil
public class SimpleRpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleRpcClientApplication.class, args);


//        URI uri = nameService.lookupService(serviceName);
//        HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
//        String response = helloService.hello(name);
//        log.info("收到响应: {}.", response);
    }
}
