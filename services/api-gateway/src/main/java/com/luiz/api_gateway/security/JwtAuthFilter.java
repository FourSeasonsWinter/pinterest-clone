package com.luiz.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private SecretKey secretKey;
    private JwtParser jwtParser;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public record AllowedRoute(HttpMethod method, String pathPattern) {
    }

    private static final List<AllowedRoute> PUBLIC_ROUTES = List.of(
            new AllowedRoute(HttpMethod.GET, "/users/**"),
            new AllowedRoute(HttpMethod.GET, "/pins/**"),
            new AllowedRoute(HttpMethod.GET, "/boards/**"),
            new AllowedRoute(HttpMethod.GET, "/pin-board/**"),
            new AllowedRoute(HttpMethod.GET, "/follows/**"),
            new AllowedRoute(HttpMethod.GET, "/likes/**"),
            new AllowedRoute(HttpMethod.POST, "/auth/**"),
            new AllowedRoute(HttpMethod.POST, "/register/**"),
            new AllowedRoute(HttpMethod.POST, "/auth/refresh-token"));

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        HttpMethod method = request.getMethod();

        if (isPublicRoute(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            jwtParser.parseSignedClaims(token);
        } catch (JwtException e) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    private boolean isPublicRoute(String path, HttpMethod method) {
        return PUBLIC_ROUTES.stream()
                .anyMatch(route -> route.method().equals(method) && pathMatcher.match(route.pathPattern(), path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
