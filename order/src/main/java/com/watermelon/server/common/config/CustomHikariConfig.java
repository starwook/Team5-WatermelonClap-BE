package com.watermelon.server.common.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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
    public HikariDataSource orderEventQuizDatasource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.addDataSourceProperty("url", url);
        hikariConfig.addDataSourceProperty("username", username);
        hikariConfig.addDataSourceProperty("password", password);
        hikariConfig.setConnectionTimeout(250L);
        //orderEventQuiz만을 위한 설정 0.1초마다 확인
        return new HikariDataSource(hikariConfig);
    }
    @Bean(name = "orderEventQuizSubmitTransactionManager")
    public DataSourceTransactionManager transactionManager1(
            @Qualifier("orderEventQuizSubmitDatasource") HikariDataSource hikariDataSource
    ) {
        return new DataSourceTransactionManager(hikariDataSource);
    }

}
