# Jessica-RPC
一个简单的学习RPC的样例，做了非常详尽的注释便于理解，最基本的功能完全基于Netty和JDK实现，麻雀虽小五脏俱全，欢迎Star一起进步～

## 整体架构
![RPC流程](https://tva1.sinaimg.cn/large/008eGmZEly1goi6ztcdzjj32bs0u0tqk.jpg)

## 工程结构

模块 | 说明
-- | --
jessica-rpc-common | 通用的类和工具等
jessica-rpc-api | RPC框架对外接口
jessica-rpc-serializer | 序列化相关接口和实现
jessica-rpc-nameservice | 注册中心客户端相关实现
jessica-rpc-core | 基于Netty的核心通信功能实现
jessica-rpc-sample | 测试用例，模拟本地RPC

## 项目特性

- 自定义协议格式，包含header和payload两部分，响应继承自请求共用一套处理方式，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-core/src/main/java/com/github/dolly0526/jessicarpc/core/transport/protocol)
- 传输层采用TCP协议，基于Netty框架进行网络编程，自定义编解码器解决粘包半包问题，以及对上述自定义协议进行编解码处理，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-core/src/main/java/com/github/dolly0526/jessicarpc/core/transport/netty)
- 客户端使用`ResponsePendingCenter`存储所有在途的请求，通过`requestId`实现异步的请求和响应的对应；服务端使用`RequestHandlerRegistry`实现对多种请求类型的统一接收，例如区分业务请求和心跳请求，这两处都是抽象和解耦的处理，见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-core/src/main/java/com/github/dolly0526/jessicarpc/core)；同时，简单使用了协程框架Quasar，避免在等待响应时阻塞线程，提高性能
- 基于JDK自带的SPI机制实现IOC，插件式开发耦合度低，更加易于扩展，也可以重写`ServiceSpiSupport`使用Spring的IOC容器，代码见[此处](https://github.com/dolly0526/jessica-rpc/blob/main/jessica-rpc-common/src/main/java/com/github/dolly0526/jessicarpc/common/support/ServiceSpiSupport.java)
- 抽象出通用的`Serializer`接口，支持序列化方式的扩展，默认使用JDK序列化；基于JSON的序列化存在bug，需要处理基本类型的返回值，以及序列化方法的参数时需要带上类型，目前只能通过Fastjson底层的类型推断进行反序列化，见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-serializer/src/main/java/com/github/dolly0526/jessicarpc/serializer)
- 支持通过字符串模版动态编译、JDK、CGLib三种方式生成Stub代理类，默认使用CGLib动态代理，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-core/src/main/java/com/github/dolly0526/jessicarpc/core/client)
- 抽象出注册中心的接口`NameService`，可以扩展多种注册中心，只要维护一个大Map任何存储介质都可以使用，目前支持本地文件和MySQL两种，默认使用JDK的File接口作为注册中心，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-nameservice/src/main/java/com/github/dolly0526/jessicarpc/nameservice)
- 实现了简单的负载均衡，支持round-robin、random两种策略，默认使用random策略，也可以继承`RouteStrategy`类进行扩展，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-nameservice/src/main/java/com/github/dolly0526/jessicarpc/nameservice/loadbalance)
- **TODO**项，可以再设计两个注解，实现更优雅的动态代理和服务注册，目前要实现需要依赖Spring的IOC和AOP，全局扫描某些注解再注册一个BeanPostProcessor处理这些注解；后续看了Dubbo后再实现这部分，如果有空还可以实现一些熔断限流等服务治理功能
- 测试用例在`jessica-rpc-sample`包，注意先启动Server再启动Client，代码见[此处](https://github.com/dolly0526/jessica-rpc/tree/main/jessica-rpc-sample)

