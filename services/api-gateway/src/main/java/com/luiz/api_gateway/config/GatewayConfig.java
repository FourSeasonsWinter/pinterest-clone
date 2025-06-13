package com.luiz.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;
    
    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/users/**", "/auth/**")
                .uri("lb://user-service"))
            .route("pin-service", r -> r.path("/pins/**")
                .filters(f -> f
                    .filter(filter)
                    )
                .uri("lb://pin-service"))
            .route("board-service", r -> r.path("/boards/**")
                .filters(f -> f
                    .filter(filter)
                    )
                .uri("lb://board-service"))
            .route("follow-service", r -> r.path("/follows/**")
                .filters(f -> f
                    .filter(filter)
                    )
                .uri("lb://follow-service"))
            .route("like-service", r -> r.path("/likes/**")
                .filters(f -> f
                    .filter(filter)
                    )
                .uri("lb://like-service"))
            .route("pin-board-service", r -> r.path("/pin-board/**")
                .filters(f -> f
                    .filter(filter)
                    )
                .uri("lb://pin-board-service"))
            .build();

    }
}
