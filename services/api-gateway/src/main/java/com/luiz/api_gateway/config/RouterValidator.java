package com.luiz.api_gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class RouterValidator {
    
    public static final List<String> openEndpoints = List.of(
        "/auth/register",
        "/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecure =
        request -> openEndpoints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
