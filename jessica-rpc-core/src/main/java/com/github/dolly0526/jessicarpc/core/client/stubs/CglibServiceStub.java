package com.github.dolly0526.jessicarpc.core.client.stubs;

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
public class CglibServiceStub<T> extends AbstractServiceStub implements MethodInterceptor {

    // 被代理的接口
    private Class<T> target;

    // cglib的方法，拦截被代理对象的所有方法
    private Enhancer enhancer = new Enhancer();


    public CglibServiceStub(Class<T> aClass) {
        this.target = aClass;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        // 构造request请求对象
        RpcRequest rpcRequest = new RpcRequest(
                target.getCanonicalName(),
                method.getName(),
                SerializeSupport.serialize(objects));

        // 远程调用该方法
        byte[] bytes = invokeRemote(rpcRequest);

        // 反序列化后返回，注意此处需要填充类型
        return SerializeSupport.parse(bytes, method.getReturnType());
    }

    /**
     * 获取代理对象
     *
     * @return
     */
    public Object getProxy(){

        // 设置需要创建子类的类
        enhancer.setSuperclass(target);
        enhancer.setCallback(this);

        // 通过字节码技术动态创建子类实例
        return enhancer.create();
    }
}
