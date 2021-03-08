package com.github.dolly0526.simplerpc.core;

import java.io.Closeable;

/**
 * RPC服务端的启停操作
 *
 * @author yusenyang
 * @create 2021/3/8 17:34
 */
public interface RpcService extends Closeable {

    /**
     * 服务端启动RPC框架，监听接口，开始提供远程服务。
     *
     * @return 服务实例，用于程序停止的时候安全关闭服务。
     */
    Closeable startServer() throws Exception;
}
