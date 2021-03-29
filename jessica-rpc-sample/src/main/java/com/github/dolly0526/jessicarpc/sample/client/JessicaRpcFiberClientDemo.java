package com.github.dolly0526.jessicarpc.sample.client;

import co.paralleluniverse.fibers.Fiber;
import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.api.RpcAccessPoint;
import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import com.github.dolly0526.jessicarpc.sample.api.HelloService;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloRequest;
import com.github.dolly0526.jessicarpc.sample.api.model.HelloResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URI;

/**
 * 客户端测试用例
 *
 * @author yusenyang
 * @create 2021/3/9 19:05
 */
@Slf4j
public class JessicaRpcFiberClientDemo {

    public static void main(String[] args) throws Throwable {

        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "jessica_rpc_name_service.data");

        // 所有rpc方法都封装在RpcAccessPoint包里
        try (RpcAccessPoint rpcAccessPoint = ServiceSpiSupport.load(RpcAccessPoint.class)) {

            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            URI uri = nameService.lookupService(serviceName);
            log.info("找到服务{}，提供者: {}.", serviceName, uri);

            // 获取代理的stub
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);

            new Fiber<String>(() -> {
                String response1 = helloService.hello("Master MQ");
                log.info("{} 收到响应: {}.", Fiber.currentFiber().getName(), response1);
            }).start();

            new Fiber<String>(() -> {
                HelloResult response2 = helloService.helloMoreResult("Dolly", 99999L);
                log.info("{} 收到响应: {}.", Fiber.currentFiber().getName(), response2);
            }).start();

            new Fiber<String>(() -> {
                HelloResult response3 = helloService.helloMoreResult(new HelloRequest(123, "123456"));
                log.info("{} 收到响应: {}.", Fiber.currentFiber().getName(), response3);
            }).start();

            Thread.sleep(100);
        }
    }
}
