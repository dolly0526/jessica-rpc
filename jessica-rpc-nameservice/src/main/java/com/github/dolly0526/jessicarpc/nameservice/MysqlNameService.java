package com.github.dolly0526.jessicarpc.nameservice;

import com.github.dolly0526.jessicarpc.api.NameService;
import com.github.dolly0526.jessicarpc.common.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yusenyang
 * @create 2021/3/11 16:05
 */
@Slf4j
@Singleton
public class MysqlNameService implements NameService {

    // 只支持mysql TODO 暂时用file替代，不想改测试用例了。。
    private static final Collection<String> schemes = Collections.singleton("file");

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

        // 判断是否存在该服务
        String sql = "SELECT * FROM vvms.t_name_service WHERE service_name = ? AND status = 1 LIMIT 1";
        List<Map<String, Object>> existService = jdbcTemplate.queryForList(sql, serviceName);

        // 如果不存在，则新增
        if (existService.size() < 1) {
            sql = "INSERT INTO vvms.t_name_service (service_name, uri) VALUES (?, ?)";
            jdbcTemplate.update(sql, serviceName, uri.toString());

        } else {
            sql = "UPDATE vvms.t_name_service SET status = 1 WHERE service_name = ?";
            jdbcTemplate.update(sql, serviceName);
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {

        // TODO 此处可以做负载均衡，先简单处理
        String sql = "SELECT * FROM vvms.t_name_service WHERE service_name = ? AND status = 1 LIMIT 1";
        List<Map<String, Object>> existService = jdbcTemplate.queryForList(sql, serviceName);

        // 如果不存在，需要返回null
        if (existService.size() > 0) {
            return URI.create(String.valueOf(existService.get(0).get("uri")));

        } else {
            return null;
        }
    }
}
