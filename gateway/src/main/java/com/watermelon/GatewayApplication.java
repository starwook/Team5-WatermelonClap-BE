package com.watermelon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
//        BlockHound.install();
        SpringApplication.run(GatewayApplication.class, args);
    }
}