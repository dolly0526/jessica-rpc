package com.github.dolly0526.jessicarpc.core.server;

import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;

/**
 * 命令处理器，用来处理各类请求，需要指定类型
 *
 * @author yusenyang
 * @create 2021/3/9 17:45
 */
public interface RequestHandler {

    /**
     * 处理请求
     *
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     */
    int type();
}
