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
        "/users/batch",
        "/pins/by-user",
        "/pins/batch",
        "/boards/by-user",
        "/follows/followers",
        "/follows/followed-by",
        "/follows/count",
        "/likes/user",
        "/likes/pin"
    );

    public Predicate<ServerHttpRequest> isSecure =
        request -> {
            String path = request.getURI().getPath();
            String method = request.getMethod().name();

            if (path.startsWith("/users") && method.equalsIgnoreCase("GET")) {
                return false;
            }

            if (path.startsWith("/pins") && method.equalsIgnoreCase("GET")) {
                return false;
            }

            if (path.startsWith("/boards") && method.equalsIgnoreCase("GET")) {
                return false;
            }

            return openEndpoints.stream().noneMatch(uri -> path.startsWith(uri));
        };
}
