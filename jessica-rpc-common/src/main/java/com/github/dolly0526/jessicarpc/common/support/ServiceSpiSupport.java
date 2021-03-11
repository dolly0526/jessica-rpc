package com.github.dolly0526.jessicarpc.common.support;

import com.github.dolly0526.jessicarpc.common.annotation.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * SPI类加载器帮助类
 *
 * @author yusenyang
 * @create 2021/3/9 11:07
 */
public class ServiceSpiSupport {

    // TODO 可以通过spring ioc等方式实现ioc
    private final static Map<String, Object> singletonServices = new HashMap<>();

    public synchronized static <S> S load(Class<S> service) {
        return StreamSupport
                .stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSpiSupport::singletonFilter)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport
                .stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSpiSupport::singletonFilter).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <S> S singletonFilter(S service) {

        if (service.getClass().isAnnotationPresent(Singleton.class)) {
            String className = service.getClass().getCanonicalName();
            Object singletonInstance = singletonServices.putIfAbsent(className, service);
            return singletonInstance == null ? service : (S) singletonInstance;

        } else {
            return service;
        }
    }
}
