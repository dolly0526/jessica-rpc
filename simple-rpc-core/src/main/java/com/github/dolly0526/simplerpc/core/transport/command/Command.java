package com.github.dolly0526.simplerpc.core.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装请求和响应数据
 *
 * @author yusenyang
 * @create 2021/3/8 18:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {

    // 消息头部
    protected Header header;

    // 消息内容
    private byte [] payload;
}
