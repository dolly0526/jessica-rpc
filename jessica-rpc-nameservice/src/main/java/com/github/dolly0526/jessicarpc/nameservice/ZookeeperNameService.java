package com.github.dolly0526.jessicarpc.nameservice;

import com.github.dolly0526.jessicarpc.api.NameService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * TODO 采用ZooKeeper作为注册中心，参考mysql的案例
 *
 * @author yusenyang
 * @create 2021/3/11 16:06
 */
public class ZookeeperNameService implements NameService {

    @Override
    public Collection<String> supportedSchemes() {
        return null;
    }

    @Override
    public void connect(URI nameServiceUri) {

    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {

    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }
}
