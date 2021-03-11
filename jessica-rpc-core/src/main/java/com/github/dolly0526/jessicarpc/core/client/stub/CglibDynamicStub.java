package com.github.dolly0526.jessicarpc.core.client.stub;

import com.github.dolly0526.jessicarpc.common.model.RpcRequest;
import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author yusenyang
 * @create 2021/3/10 20:46
 */
public class CglibDynamicStub<T> extends AbstractServiceStub implements MethodInterceptor {

    // 被代理的接口
    private Class<T> serviceClass;

    // cglib的方法，拦截被代理对象的所有方法
    private Enhancer enhancer = new Enhancer();


    public CglibDynamicStub(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        // 构造request请求对象
        RpcRequest rpcRequest = new RpcRequest(
                serviceClass.getCanonicalName(),
                method.getName(),
                SerializeSupport.serialize(objects));

        // 远程调用该方法
        byte[] bytes = invokeRemote(rpcRequest);

        // 反序列化后返回，注意此处需要填充类型
        // TODO 基本类型、包装类、void等怎么处理？
        return SerializeSupport.parse(bytes, method.getReturnType());
    }
}
