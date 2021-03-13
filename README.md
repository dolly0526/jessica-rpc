# Jessica-RPC
A simple demo RPC framework based on Netty And JDK.

## 整体架构
![RPC流程](https://tva1.sinaimg.cn/large/008eGmZEly1goi6ztcdzjj32bs0u0tqk.jpg)

## 项目结构

Module | 说明
-- | --
jessica-rpc-common | 通用的类和工具等
jessica-rpc-api | RPC框架对外接口
jessica-rpc-serializer | 序列化相关接口和实现
jessica-rpc-nameservice | 注册中心客户端相关实现
jessica-rpc-core | 基于Netty的核心通信功能实现
jessica-rpc-sample | 测试用例，模拟本地RPC
