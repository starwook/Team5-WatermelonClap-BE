package com.watermelon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

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