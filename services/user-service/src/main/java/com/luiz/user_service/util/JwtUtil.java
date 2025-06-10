package com.luiz.user_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

  @SuppressWarnings("unused")
  private final String secret;
  private final long expiration;
  private final SecretKey key;

  public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
    this.secret = secret;
    this.expiration = expiration;
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(UUID userId) {
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}