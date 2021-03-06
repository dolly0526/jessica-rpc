package com.github.dolly0526.jessicarpc.common.model;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 * 注册中心的元数据
 *
 * @author yusenyang
 * @create 2021/3/9 11:10
 */
public class Metadata extends HashMap<String /*服务名*/, List<URI>/*服务提供者URI列表*/> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metadata:").append("\n");

        for (Entry<String, List<URI>> entry : entrySet()) {

            // 服务名
            sb.append("\t").append("Classname: ").append(entry.getKey()).append("\n");

            // 服务提供者的uri
            sb.append("\t").append("URIs:").append("\n");
            for (URI uri : entry.getValue()) {
                sb.append("\t\t").append(uri).append("\n");
            }
        }

        return sb.toString();
    }
}
