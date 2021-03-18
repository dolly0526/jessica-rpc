package com.github.dolly0526.jessicarpc.core.transport.impl.socket;

import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;
import com.github.dolly0526.jessicarpc.core.transport.TransportServer;
import com.github.dolly0526.jessicarpc.core.transport.impl.socket.handler.RequestHandlerThread;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author yusenyang
 * @create 2021/3/17 18:59
 */
@Slf4j
public class SocketServer implements TransportServer {

    // 管理所有命令处理器
    private RequestHandlerRegistry requestHandlerRegistry;

    // 处理请求的线程池
    private ExecutorService workerThreadPool;

    // 基于BIO的ServerSocket对象
    private ServerSocket serverSocket;


    // spi加载的时候调用无参构造器，初始化requestHandlerRegistry
    public SocketServer() {
        requestHandlerRegistry = RequestHandlerRegistry.getInstance();
    }


    @Override
    public void start(int port) throws Exception {

        // 初始化工作线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.initialize();
        workerThreadPool = taskExecutor.getThreadPoolExecutor();

        // 服务端绑定端口
        serverSocket = new ServerSocket(port);

        // 循环监听，单独开一个线程防止主线程阻塞
        Thread bossThread = new Thread(() -> {
            while (true) {
                Socket socket = null;

                try {
                    // 阻塞监听，等待客户端连接
                    socket = serverSocket.accept();
                    // 关闭nagle算法
                    socket.setTcpNoDelay(true);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 每次accept之后，worker线程池才开始执行任务
                workerThreadPool.execute(new RequestHandlerThread(socket, requestHandlerRegistry));
            }
        });

        // 设置为后台线程
        bossThread.setDaemon(true);
        bossThread.start();
    }

    @SneakyThrows
    @Override
    public void close() {
        if (workerThreadPool != null) {
            workerThreadPool.shutdown();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
