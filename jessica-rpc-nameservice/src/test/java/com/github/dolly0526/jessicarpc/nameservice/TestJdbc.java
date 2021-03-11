package com.github.dolly0526.jessicarpc.nameservice;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * @author yusenyang
 * @create 2021/3/11 17:39
 */
public class TestJdbc {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(
            new SingleConnectionDataSource("jdbc:mysql://localhost:3306/vvms",
                    "root", "root", true));


    @Test
    public void testJdbc() {

        System.out.println(jdbcTemplate.queryForList("show tables"));
    }
}
