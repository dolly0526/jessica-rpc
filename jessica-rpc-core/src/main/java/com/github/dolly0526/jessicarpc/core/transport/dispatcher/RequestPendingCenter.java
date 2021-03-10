package com.github.dolly0526.jessicarpc.core.transport.dispatcher;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 将所有发出去的响应池化，响应发回时根据id获取，再把值塞进去，同时也做了一些限流
 *
 * @author yusenyang
 * @create 2021/3/8 20:33
 */
public class RequestPendingCenter implements Closeable {

    // 依靠信号量实现背压机制，限制请求并发量
    private final Semaphore semaphore = new Semaphore(10);

    // 存放所有在途的响应Future
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    // 兜底超时的机制，保证Future正常结束
    private final static long TIMEOUT_SEC = 10L;

    // 使用定时执行的线程池来清理超时请求
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    // 保留一个future对象用来关闭定时任务
    private final ScheduledFuture scheduledFuture;


    public RequestPendingCenter() {

        // 构造该对象时开启定时任务
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }


    /**
     * 将当前请求的responseFuture对象放进Center，后续需要使用再拿出来
     */
    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {

        // put前先去尝试获取限流器的许可，否则一直等待
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);

        } else {
            throw new TimeoutException();
        }
    }

    /**
     * 请求结束，从center中删除对应的future对象，并返回
     */
    public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.remove(requestId);

        if (future != null) {
            semaphore.release();
        }
        return future;
    }

    /**
     * 便利futureMap，释放所有的超时请求
     */
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

    @Override
    public void close() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
