package com.github.dolly0526.simplerpc.core.transport;

import com.github.dolly0526.simplerpc.core.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 通信相关方法
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
