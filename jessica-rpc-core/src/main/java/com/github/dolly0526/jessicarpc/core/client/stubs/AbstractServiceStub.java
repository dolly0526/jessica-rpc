package com.github.dolly0526.jessicarpc.core.client.stubs;

import com.github.dolly0526.jessicarpc.core.client.ServiceType;
import com.github.dolly0526.jessicarpc.common.util.RequestIdSupport;
import com.github.dolly0526.jessicarpc.core.client.ServiceStub;
import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Code;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Command;
import com.github.dolly0526.jessicarpc.core.transport.protocol.Header;
import com.github.dolly0526.jessicarpc.core.transport.protocol.ResponseHeader;
import com.github.dolly0526.jessicarpc.common.model.RpcRequest;

import java.util.concurrent.ExecutionException;

/**
 * @author yusenyang
 * @create 2021/3/9 15:11
 */
public abstract class AbstractServiceStub implements ServiceStub {

    // 桩的传输方法
    protected Transport transport;


    /**
     * 和invoke类似的方法，代理对象调用远端的服务
     */
    protected byte[] invokeRemote(RpcRequest request) {

        // 根据协议构造请求头
        Header header = new Header(ServiceType.TYPE_RPC_REQUEST, 1, RequestIdSupport.next());

        // 根据协议处理请求内容
        byte[] payload = SerializeSupport.serialize(request);

        // 构造请求对象
        Command requestCommand = new Command(header, payload);

        try {
            // 利用transport对象发送给远端，并阻塞获取结果
            Command responseCommand = transport.send(requestCommand).get();

            // 处理响应头部
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();

            // 如果响应成功，则继续进行反序列化
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();

            } else {
                throw new Exception(responseHeader.getError());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
