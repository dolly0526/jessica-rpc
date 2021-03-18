package com.github.dolly0526.jessicarpc.core.transport.impl.socket;

import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec.RequestEncoder;
import com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec.ResponseDecoder;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * 基于socket实现的客户端通信接口
 *
 * @author yusenyang
 * @create 2021/3/17 18:52
 */
@Slf4j
public class SocketTransport implements Transport {

    // 服务地址
    private SocketAddress address;

    // 连接超时时间
    private int connectionTimeout;


    // TODO 不需要每次请求都获取一个socket，可以复用连接
    public SocketTransport(SocketAddress address, int connectionTimeout) {
        this.address = address;
        this.connectionTimeout = connectionTimeout;
    }


    @SneakyThrows
    @Override
    public CompletableFuture<Command> send(Command request) {

        // 构建返回值，此处实际上只能同步
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();

        // 每次初始化一个新的socket
        Socket socket = new Socket();
        // 关闭nagle算法
        socket.setTcpNoDelay(true);
        // 连接socket，必须先连接再获取输入输出流！
        socket.connect(address, connectionTimeout);

        // 从socket中获取输入输出流
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            // 写出请求
            RequestEncoder requestEncoder = new RequestEncoder();
            requestEncoder.encode(outputStream, request);

            // 接收响应
            ResponseDecoder responseDecoder = new ResponseDecoder();
            Command response = responseDecoder.decode(inputStream);

            // 封装到future中
            completableFuture.complete(response);

        } catch (Throwable t) {

            // 处理发送异常
            completableFuture.completeExceptionally(t);
        } finally {

            // 用完之后关闭socket
            socket.close();
        }

        return completableFuture;
    }
}
