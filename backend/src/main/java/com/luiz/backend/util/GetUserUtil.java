package com.luiz.backend.util;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

@Component
public class GetUserUtil {
    
    private final WebClient userWebClient;

    public GetUserUtil(WebClient.Builder webClientBuilder) {
        this.userWebClient = webClientBuilder.baseUrl("http://api-gateway").build();
    }
    
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            throw new UnauthenticatedException("There is no logged in user");
        }
        
        String username = authentication.getName();
        return getUserByUsername(username);
    }
    
    public User getUserByUsername(String username) {
        return fetchUserByNameFromService(username)
            .blockOptional()
            .orElseThrow();
    }
    
    public User getUserById(UUID id) {
        return fetchUserByIdFromService(id)
            .blockOptional()
            .orElseThrow();
    }
    
    private Mono<User> fetchUserByNameFromService(String username) {
        return userWebClient.get()
            .uri("/USER-SERVICE/users/{username}", username)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals,
                response -> Mono.error(new UserNotFoundException("User not found with username " + username)))
            .onStatus(HttpStatusCode::isError,
                response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new RuntimeException("Error from user service: " + response.statusCode() + " - " + errorBody))))
            .bodyToMono(User.class);
    }

    private Mono<User> fetchUserByIdFromService(UUID id) {
        return userWebClient.get()
            .uri("/USER-SERVICE/users/{id}", id)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals,
                response -> Mono.error(new UserNotFoundException("User not found with id " + id)))
            .onStatus(HttpStatusCode::isError,
                response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new RuntimeException("Error from user service: " + response.statusCode() + " - " + errorBody))))
            .bodyToMono(User.class);
    }
}
