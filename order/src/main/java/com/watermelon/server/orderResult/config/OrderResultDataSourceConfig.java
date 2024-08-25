package com.watermelon.server.orderResult.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "orderResultEntityManager",
        transactionManagerRef = "orderResultTransactionManager",
        basePackages = {"com.watermelon.server.orderResult.repository"}
)
@Configuration
@Profile("!local")
public class OrderResultDataSourceConfig {
    @Value("${spring.datasource1.url}")
    private String url;

    @Value("${spring.datasource1.username}")
    private String username;

    @Value("${spring.datasource1.password}")
    private String password;

    private int maximumPoolSize =10;

    @Bean(name = "orderResultDatasource")
    public HikariDataSource orderResultDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setConnectionTimeout(250L);
        hikariConfig.setPoolName("orderResultPool");
        //orderEventQuiz만을 위한 설정 0.1초마다 확인
        return new HikariDataSource(hikariConfig);
    }
    @Bean(name ="orderResultEntityManager")
    public LocalContainerEntityManagerFactoryBean orderResultEntityManager(EntityManagerFactoryBuilder builder)  {
        Map<String,Object> properties = new HashMap<>();
        properties.put("dialect", "org.hibernate.dialect.MySQL8InnoDBDialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
//        properties.put("hibernate.ddl-auto", "create");
        properties.put("open_in_view", "false");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.physical_naming_strategy" , "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        return builder
                .dataSource(orderResultDataSource())
                .packages("com.watermelon.server.orderResult")
                .properties(properties)
                .build();
    }
    @Bean(name = "orderResultTransactionManager")
    public JpaTransactionManager orderResultTransactionManager(
            @Qualifier("orderResultEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
