package com.opentranslation.management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

  private static final String KEY = "super-secret-key-super-secret-key-12345"; // make it long enough (>= 256 bits)
  private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));
  private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24h

  public String generateToken(String clientCode) {
    return Jwts.builder()
               .setSubject(clientCode)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
               .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // <- note the order and types
               .compact();
  }

  public String extractClientCode(String token) {
    return Jwts.parserBuilder()
               .setSigningKey(SECRET_KEY)
               .build()
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      extractClientCode(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
