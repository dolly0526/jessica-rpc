package com.github.dolly0526.jessicarpc.core.client.impl;

import com.github.dolly0526.jessicarpc.core.client.ServiceStub;
import com.github.dolly0526.jessicarpc.core.client.StubFactory;
import com.github.dolly0526.jessicarpc.core.transport.Transport;
import com.itranswarp.compiler.JavaStringCompiler;

import java.util.Map;

/**
 * 通过动态生成字节码文件进行代理
 *
 * @author yusenyang
 * @create 2021/3/9 15:18
 */
public class TemplateStubFactory implements StubFactory {
    private final static String STUB_SOURCE_TEMPLATE =
            "package com.github.dolly0526.jessicarpc.core.client.stubs;\n" +
            "import com.github.dolly0526.jessicarpc.common.model.RpcRequest;\n" +
            "import com.github.dolly0526.jessicarpc.serializer.SerializeSupport;\n" +
            "\n" +
            "public class %s extends AbstractServiceStub implements %s {\n" +
            "    @Override\n" +
            "    public String %s(String arg) {\n" +
            "        return SerializeSupport.parse(\n" +
            "                invokeRemote(\n" +
            "                        new RpcRequest(\n" +
            "                                \"%s\",\n" +
            "                                \"%s\",\n" +
            "                                SerializeSupport.serialize(arg)\n" +
            "                        )\n" +
            "                )\n" +
            "        );\n" +
            "    }\n" +
            "}";


    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {

        try {
            // 填充模板
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "com.github.dolly0526.jessicarpc.core.client.stubs." + stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();

            // 编译源代码
            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName, methodName, classFullName, methodName);
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);

            // 加载编译好的类
            Class<?> clazz = compiler.loadClass(stubFullName, results);

            // 把Transport赋值给桩
            ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
            stubInstance.setTransport(transport);

            // 返回这个桩
            return (T) stubInstance;

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
