package com.anurag.notifyhub.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  final private Long expirationTime = 1000L * 60 * 60 * 24;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String generateToken(String email) {
    String generatedToken = Jwts
        .builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(getSigningKey()).compact();
    log.info("Token generated | email={}", email);
    return generatedToken;
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .verifyWith(getSigningKey())
        .build().parseSignedClaims(token)
        .getPayload();
  }

  public boolean isTokenValid(String token) {
    return !extractAllClaims(token).getExpiration().before(new Date());
  }

  public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

}
