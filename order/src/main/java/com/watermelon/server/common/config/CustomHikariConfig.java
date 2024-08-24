package com.watermelon.server.common.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CustomHikariConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "orderEventQuizSubmitDatasource")
    public DataSource orderEventQuizDatasource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.addDataSourceProperty("url", url);
        hikariConfig.addDataSourceProperty("username", username);
        hikariConfig.addDataSourceProperty("password", password);
        hikariConfig.setConnectionTimeout(100L);
        //orderEventQuiz만을 위한 설정 0.1초마다 확인
        return new HikariDataSource(hikariConfig);
    }
}
