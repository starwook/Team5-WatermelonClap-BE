//package com.watermelon.server.common.config;
//
//
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
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "orderResultEntityManager",
//        transactionManagerRef = "orderResultTransactionManager",
//        basePackages = {"com.watermelon.server.order.repository"}
//)
//public class HikariConfig {
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    private int maximumPoolSize =10;
//
//    @Bean
//    @Primary
//    public HikariDataSource orderDataSource() {
//        com.zaxxer.hikari.HikariConfig hikariConfig = new com.zaxxer.hikari.HikariConfig();
//        hikariConfig.setJdbcUrl(url);
//        hikariConfig.setUsername(username);
//        hikariConfig.setPassword(password);
//        hikariConfig.setMaximumPoolSize(maximumPoolSize);
//        return new HikariDataSource(hikariConfig);
//    }
//    @Bean(name = "orderResultDatasource")
//    public HikariDataSource orderResultDataSource() {
//        com.zaxxer.hikari.HikariConfig hikariConfig = new com.zaxxer.hikari.HikariConfig();
//        hikariConfig.setJdbcUrl(url);
//        hikariConfig.setUsername(username);
//        hikariConfig.setPassword(password);
//        hikariConfig.setMaximumPoolSize(maximumPoolSize);
//        hikariConfig.setConnectionTimeout(250L);
//        //orderEventQuiz만을 위한 설정 0.1초마다 확인
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
////        LocalContainerEntityManagerFactoryBean orderManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
////        orderManagerFactoryBean.setDataSource(orderDataSource());
////        orderManagerFactoryBean.setPackagesToScan("com/watermelon/server/order");
////        orderManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
////        orderManagerFactoryBean.setPersistenceUnitName("order");
////        return orderManagerFactoryBean;
//    }
//
//    @Bean(name ="orderResultEntityManager")
//    public LocalContainerEntityManagerFactoryBean orderResultEntityManager(EntityManagerFactoryBuilder builder)  {
//        return builder
//                .dataSource(orderResultDataSource())
//                .packages("com/watermelon/server/order")
//                .persistenceUnit("orderResult")
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public JpaTransactionManager orderTransactionManager(
//            EntityManagerFactory entityManagerFactory
//    ) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean(name = "orderResultTransactionManager")
//    public JpaTransactionManager orderResultTransactionManager(
//            EntityManagerFactory entityManagerFactory
//    ) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}
