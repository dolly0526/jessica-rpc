package com.github.dolly0526.simplerpc.common.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author yusenyang
 * @create 2021/3/9 10:33
 */
public interface SimpleRpcConst {

    // 统一使用UTF-8字符集
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 长度字段，用于解决粘包半包问题
    int DEFAULT_LENGTH_FIELD = Integer.BYTES;
}
