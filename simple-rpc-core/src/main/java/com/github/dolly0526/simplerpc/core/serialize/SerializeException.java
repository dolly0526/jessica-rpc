package com.github.dolly0526.simplerpc.core.serialize;

/**
 * @author yusenyang
 * @create 2021/3/8 18:15
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}
