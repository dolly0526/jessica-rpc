package com.github.dolly0526.jessicarpc.core.transport.impl.socket.handler;

import com.github.dolly0526.jessicarpc.core.server.RequestHandler;
import com.github.dolly0526.jessicarpc.core.server.RequestHandlerRegistry;
import com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec.RequestDecoder;
import com.github.dolly0526.jessicarpc.core.transport.impl.socket.codec.ResponseEncoder;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author yusenyang
 * @create 2021/3/17 21:15
 */
@Slf4j
public class RequestHandlerThread implements Runnable {

    // 服务端连接到的socket
    private Socket socket;

    // 存放所有请求处理器，初始化时传入
    private final RequestHandlerRegistry requestHandlerRegistry;


    public RequestHandlerThread(Socket socket, RequestHandlerRegistry requestHandlerRegistry) {
        this.socket = socket;
        this.requestHandlerRegistry = requestHandlerRegistry;
    }


    @SneakyThrows
    @Override
    public void run() {
        if (socket == null || socket.isClosed()) return;

        // 从socket中获取输入输出流
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            // 反序列化请求
            RequestDecoder requestDecoder = new RequestDecoder();
            Command request = requestDecoder.decode(inputStream);

            // 此时请求已完成解码，根据type从命令注册中心获取对应的handler
            RequestHandler handler = requestHandlerRegistry.get(request.getHeader().getType());
            if (handler != null) {

                // 用handler处理请求
                Command response = handler.handle(request);
                if (response != null) {

                    // 写出响应
                    ResponseEncoder responseEncoder = new ResponseEncoder();
                    responseEncoder.encode(outputStream, response);

                } else {
                    log.warn("Response is null!");
                }
            } else {
                throw new Exception(String.format("No handler for request with type: %d!", request.getHeader().getType()));
            }
        } finally {

            // 用完之后直接关闭socket
            socket.close();
        }
    }
}
