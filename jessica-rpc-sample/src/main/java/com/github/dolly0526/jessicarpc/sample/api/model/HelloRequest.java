package com.github.dolly0526.jessicarpc.sample.api.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class HelloRequest implements Serializable {

    private Object param;
    private String timestamp;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HelloRequest{" +
                "param=" + param +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
