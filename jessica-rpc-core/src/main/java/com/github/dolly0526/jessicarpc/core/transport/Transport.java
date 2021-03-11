package com.github.dolly0526.jessicarpc.core.transport;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 客户端通信相关的方法，每个客户端可以持有多个
 *
 * @author yusenyang
 * @create 2021/3/8 18:34
 */
public interface Transport {

    /**
     * 异步方法，发送请求命令，把请求数据发出去之后就返回；返回一个future对象，用于客户端获取对应的结果
     *
     * @param request 请求命令
     * @return 返回值是一个Future
     */
    CompletableFuture<Command> send(Command request);
}
