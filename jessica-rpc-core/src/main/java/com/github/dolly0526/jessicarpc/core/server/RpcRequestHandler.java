package com.github.dolly0526.jessicarpc.core.server;

import com.github.dolly0526.jessicarpc.api.spi.Singleton;
import com.github.dolly0526.jessicarpc.common.model.RpcRequest;
import com.github.dolly0526.jessicarpc.core.client.ServiceType;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Code;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import com.github.dolly0526.jessicarpc.core.transport.protocol.ResponseHeader;
import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yusenyang
 * @create 2021/3/9 17:50
 */
@Slf4j
@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {

    // 获取所有的服务提供方
    private Map<String/*service name*/, Object/*service provider*/> serviceProviders = new HashMap<>();


    @Override
    public Command handle(Command requestCommand) {

        // 获取请求头
        Header header = requestCommand.getHeader();

        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());

        try {
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());

            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                Object result = invokeMultiArgsMethod(rpcRequest, serviceProvider);
//                String result = invokeSingleStringArgMethod(rpcRequest, serviceProvider);

                // 把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(result));
            }

            // 如果没找到，返回NO_PROVIDER错误响应。
            log.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);

        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            log.warn("Exception: ", t);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceType.TYPE_RPC_REQUEST;
    }

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        log.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }


    /**
     * 支持多个任意类型参数的方法
     */
    private Object invokeMultiArgsMethod(RpcRequest rpcRequest, Object serviceProvider) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object[] arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
        Class[] paraTypes = new Class[arg.length];
        for (int i = 0; i < paraTypes.length; i++) {
            paraTypes[i] = arg[i].getClass();
        }
        Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), paraTypes);
        return method.invoke(serviceProvider, arg);
    }

    /**
     * 仅支持单个string类型的参数，已废弃
     */
    @Deprecated
    private String invokeSingleStringArgMethod(RpcRequest rpcRequest, Object serviceProvider) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
        Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
        return (String) method.invoke(serviceProvider, arg);
    }
}
