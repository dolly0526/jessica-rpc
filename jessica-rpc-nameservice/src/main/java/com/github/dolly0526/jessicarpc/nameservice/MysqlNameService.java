package com.github.dolly0526.jessicarpc.nameservice;

import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import com.github.dolly0526.jessicarpc.nameservice.loadbalance.LoadBalanceSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @author yusenyang
 * @create 2021/3/11 16:05
 */
@Slf4j
@Singleton
public class MysqlNameService implements NameService {

    // 目前只支持mysql的jdbc
    private static final Collection<String> schemes = Collections.singleton("mysql");

    // 操作数据库
    private JdbcTemplate jdbcTemplate;


    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {

        // 连接到注册中心，生成jdbcTemplate对象即可
        if (schemes.contains(nameServiceUri.getScheme())) {

            // TODO 此处应当可配置，测试用例就简单处理算了
            DriverManagerDataSource dataSource = new SingleConnectionDataSource(
                    "jdbc:mysql://localhost:3306/vvms",
                    "root", "root", true);

            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            jdbcTemplate = new JdbcTemplate(dataSource);

        } else {
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {

        // 判断是否存在该服务，服务名和uri都应该满足条件
        String sql = "SELECT * FROM vvms.t_name_service WHERE service_name = ? AND uri = ?";
        String uriStr = uri.toString();
        List<Map<String, Object>> services = jdbcTemplate.queryForList(sql, serviceName, uriStr);

        // 如果不存在，则新增
        if (services.size() == 0) {
            sql = "INSERT INTO vvms.t_name_service (service_name, uri) VALUES (?, ?)";
            jdbcTemplate.update(sql, serviceName, uriStr);
            log.info("新增服务: {}, URI: {}...", serviceName, uriStr);
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {

        // 查询所有服务
        String sql = "SELECT * FROM vvms.t_name_service WHERE service_name = ?";
        List<Map<String, Object>> services = jdbcTemplate.queryForList(sql, serviceName);

        // 获取某个服务
        List<URI> uri = new ArrayList<URI>(services.size()) {{
            services.forEach(service -> add(URI.create(String.valueOf(service.get("uri")))));
        }};

        // 此处实现负载均衡
        return LoadBalanceSupport.route(uri);
    }
}
