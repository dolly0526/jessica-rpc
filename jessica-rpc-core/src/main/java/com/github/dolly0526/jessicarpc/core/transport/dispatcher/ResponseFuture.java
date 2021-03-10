package com.github.dolly0526.jessicarpc.core.transport.dispatcher;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

/**
 * 接收返回的回调对象
 *
 * @author yusenyang
 * @create 2021/3/8 20:42
 */
@Getter
public class ResponseFuture {

    // 生成的请求id
    private final int requestId;

    // 封装响应
    private final CompletableFuture<Command> future;

    // 时间戳，用来衡量请求是否超时
    private final long timestamp;


    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }
}
