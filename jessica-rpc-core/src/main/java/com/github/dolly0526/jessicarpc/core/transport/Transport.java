package com.github.dolly0526.jessicarpc.core.transport;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 客户端通信相关的方法
 *
 * @author yusenyang
 * @create 2021/3/8 18:34
 */
public interface Transport {

    /**
     * 发送请求命令
     * @param request 请求命令
     * @return 返回值是一个Future，Future
     */
    CompletableFuture<Command> send(Command request);
}
