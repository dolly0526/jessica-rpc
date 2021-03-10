package com.github.dolly0526.simplerpc.core.client.stubs;

import lombok.Getter;

/**
 * 封装rpc请求内容
 *
 * @author yusenyang
 * @create 2021/3/9 15:14
 */
@Getter
public class RpcRequest {

    // 接口全类名
    private final String interfaceName;

    // 方法名
    private final String methodName;

    // 序列化后的参数
    private final byte[] serializedArguments;


    public RpcRequest(String interfaceName, String methodName, byte[] serializedArguments) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArguments = serializedArguments;
    }
}
