package com.watermelon.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("deploy")
public class DeployConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order", r -> r
                        .path("/admin/event/order/**")
                        .or()
                        .path("/event/order/**")
                        .uri("http://10.1.5.40:8080"))
                .route("lottery", r -> r.path("/**")
                        .uri("http://10.1.5.40:8080"))
                .build();
    }
}
