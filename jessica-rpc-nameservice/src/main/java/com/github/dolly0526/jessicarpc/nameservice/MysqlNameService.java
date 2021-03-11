package com.github.dolly0526.jessicarpc.nameservice;

import com.github.dolly0526.jessicarpc.api.NameService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * @author yusenyang
 * @create 2021/3/11 16:05
 */
public class MysqlNameService implements NameService {

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
