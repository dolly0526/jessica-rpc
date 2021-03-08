package com.github.dolly0526.simplerpc.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleRpcServerApplication.class, args);

//        rpcAccessPoint.startServer();
//        URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
//        nameService.registerService(serviceName, uri);
    }
}
