package com.github.dolly0526.jessicarpc.sample.client;

import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.api.RpcAccessPoint;
import com.github.dolly0526.jessicarpc.api.spi.ServiceSupport;
import com.github.dolly0526.jessicarpc.sample.api.HelloService;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author yusenyang
 * @create 2021/3/9 19:05
 */
@Slf4j
public class SimpleRpcClientDemo {

    public static void main(String[] args) throws IOException {

        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "jessica_rpc_name_service.data");
        String name = "Master MQ";

        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {

            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;

            URI uri = nameService.lookupService(serviceName);
            assert uri != null;
            log.info("找到服务{}，提供者: {}.", serviceName, uri);

            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            log.info("请求服务, name: {}...", name);

//            String response1 = helloService.hello(name);
//            log.error("收到响应: {}.", response1);

            HelloResult response2 = helloService.helloMoreResult(name, "你好");
            log.error("收到响应: {}.", response2);
        }
    }
}
