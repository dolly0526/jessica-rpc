package com.github.dolly0526.simplerpc.core.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 通过响应分发获取响应
 *
 * @author yusenyang
 * @create 2021/3/8 20:33
 */
public class InFlightRequests implements Closeable {

    // 背压机制，限制请求并发量
    private final Semaphore semaphore = new Semaphore(10);

    // 存放在途的响应Future
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    // 兜底超时的机制，保证Future正常结束
    private final static long TIMEOUT_SEC = 10L;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture scheduledFuture;


    public InFlightRequests() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }


    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                semaphore.release();
                return true;
            } else {
                return false;
            }
        });
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.remove(requestId);
        if (null != future) {
            semaphore.release();
        }
        return future;
    }

    @Override
    public void close() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
