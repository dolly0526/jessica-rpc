package com.github.dolly0526.jessicarpc.core.transport.protocol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义协议，通用请求头部
 *
 * @author yusenyang
 * @create 2021/3/8 18:37
 */
@Getter
@Setter
@NoArgsConstructor
public class Header {

    // 唯一标识一个请求命令
    private int requestId;

    // 标识这条命令的版本号
    private int version;

    // 标识这条命令的类型
    private int type;


    /**
     * 该请求头部的长度，即为上述3个int字段的长度
     */
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }


    public Header(int type, int version, int requestId) {
        this.requestId = requestId;
        this.type = type;
        this.version = version;
    }
}
