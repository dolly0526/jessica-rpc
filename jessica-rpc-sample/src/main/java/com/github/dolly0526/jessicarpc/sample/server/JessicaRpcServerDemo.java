package com.github.dolly0526.jessicarpc.sample.server;

import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.api.RpcAccessPoint;
import com.github.dolly0526.jessicarpc.common.support.ServiceSpiSupport;
import com.github.dolly0526.jessicarpc.sample.api.HelloService;
import com.github.dolly0526.jessicarpc.sample.api.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

/**
 * 服务端测试用例
 *
 * @author yusenyang
 * @create 2021/3/9 19:06
 */
@Slf4j
public class JessicaRpcServerDemo {

    public static void main(String[] args) throws Exception {

        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "jessica_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        log.info("创建并启动RpcAccessPoint...");

        // 所有rpc方法都封装在RpcAccessPoint包里
        try (RpcAccessPoint rpcAccessPoint = ServiceSpiSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer()) {

            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            log.info("向RpcAccessPoint注册{}服务...", serviceName);

            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            log.info("服务名: {}, 向NameService注册...", serviceName);

            nameService.registerService(serviceName, uri);
            log.info("开始提供服务，按任何键退出...");

            // noinspection ResultOfMethodCallIgnored
            System.in.read();
            log.info("Bye!");

        } finally {
            boolean delete = file.delete();
            if (delete) log.info("成功删除文件，已退出...");
        }
    }
}
