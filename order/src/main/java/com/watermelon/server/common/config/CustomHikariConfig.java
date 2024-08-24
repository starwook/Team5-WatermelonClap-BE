package com.watermelon.server.common.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    private int maximumPoolSize =10;

    @Bean
    @Primary
    public HikariDataSource Datasource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        return new HikariDataSource(hikariConfig);
    }
    @Primary
    @Bean
    public DataSourceTransactionManager transactionManager(
            HikariDataSource hikariDataSource
    ) {
        return new DataSourceTransactionManager(hikariDataSource);
    }


    @Bean(name = "orderEventQuizSubmitDatasource")
    public HikariDataSource orderEventQuizDatasource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
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
