package com.luiz.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;
    
    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/users/**", "/auth/**")
                .filters(f -> f.filter(filter))
                .uri("lb://user-service"))
            .route("pin-service", r -> r.path("/pins/**")
                .filters(f -> f.filter(filter))
                .uri("lb://pin-service"))
            .build();

    }
}
