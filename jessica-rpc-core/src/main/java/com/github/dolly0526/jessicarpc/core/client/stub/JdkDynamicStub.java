package com.github.dolly0526.jessicarpc.core.client.stub;

import co.paralleluniverse.fibers.SuspendExecution;
import com.github.dolly0526.jessicarpc.common.model.RpcRequest;
import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yusenyang
 * @create 2021/3/11 10:19
 */
public class JdkDynamicStub<T> extends AbstractServiceStub implements InvocationHandler {

    // 被代理的接口
    private Class<T> serviceClass;


    public JdkDynamicStub(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, SuspendExecution {

        // 构造request请求对象
        RpcRequest rpcRequest = new RpcRequest(
                serviceClass.getCanonicalName(),
                method.getName(),
                SerializeSupport.serialize(args));

        // 远程调用该方法
        byte[] bytes = invokeRemote(rpcRequest);

        // 反序列化后返回，注意此处需要填充类型
        return SerializeSupport.parse(bytes, method.getReturnType());
    }
}
