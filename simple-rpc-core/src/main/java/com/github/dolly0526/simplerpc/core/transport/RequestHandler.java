package com.github.dolly0526.simplerpc.core.transport;

import com.github.dolly0526.simplerpc.core.transport.command.Command;

/**
 * @author yusenyang
 * @create 2021/3/9 17:45
 */
public interface RequestHandler {

    /**
     * 处理请求
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     */
    int type();
}
