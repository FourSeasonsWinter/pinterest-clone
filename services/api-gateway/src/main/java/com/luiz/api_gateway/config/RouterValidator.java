package com.luiz.api_gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {
    
    public static final List<String> openEndpoints = List.of(
        "/auth/register",
        "/auth/login",
        "/pins/by-user"
    );

    public Predicate<ServerHttpRequest> isSecure =
        request -> {
            String path = request.getURI().getPath();
            String method = request.getMethod().name();

            if (path.equals("/users/me")) {
                return true;
            }

            if (path.startsWith("/pins") && method.equalsIgnoreCase("GET")) {
                return false;
            }

            return openEndpoints.stream().noneMatch(uri -> path.startsWith(uri));
        };
}
