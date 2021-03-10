package com.github.dolly0526.simplerpc.core.transport.netty.dispatcher;

import com.github.dolly0526.simplerpc.core.transport.protocol.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 接收返回的回调对象
 *
 * @author yusenyang
 * @create 2021/3/8 20:42
 */
public class ResponseFuture {

    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;


    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        timestamp = System.nanoTime();
    }


    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
