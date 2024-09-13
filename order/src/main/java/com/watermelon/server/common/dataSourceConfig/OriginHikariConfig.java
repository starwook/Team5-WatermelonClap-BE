//package com.watermelon.server.common.config;
//
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "orderEntityManager",
//        transactionManagerRef = "orderTransactionManager",
//        basePackages = {"com.watermelon.server.order.repository"}
//)
//@Configuration
//public class OriginHikariConfig {
//    @Value("${spring.datasource1.url}")
//    private String url;
//
//    @Value("${spring.datasource1.username}")
//    private String username;
//
//    @Value("${spring.datasource1.password}")
//    private String password;
//
//    private int maximumPoolSize =10;
//
//    @Bean
//    @Primary
//    public HikariDataSource orderDataSource() {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(url);
//        hikariConfig.setUsername(username);
//        hikariConfig.setPassword(password);
//        hikariConfig.setMaximumPoolSize(maximumPoolSize);
//        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean orderEntityManager(EntityManagerFactoryBuilder builder)  {
//        return builder
//                .dataSource(orderDataSource())
//                .packages("com/watermelon/server/order")
//                .persistenceUnit("order")
//                .build();
//    }
//    @Bean
//    @Primary
//    public JpaTransactionManager orderTransactionManager(
//            EntityManagerFactory entityManagerFactory
//    ) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}
