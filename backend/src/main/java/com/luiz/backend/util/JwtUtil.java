package com.luiz.backend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
  private static final String SECRET = "123412341234123412341234123412341234123412341234";
  private static final long EXPIRATION = 1000 * 60 * 60 * 24;

  private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
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