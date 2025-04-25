package com.challenge.encomendas.encomendas.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.io.Decoders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        // retorna SecretKey
        this.expiration = expiration;
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return getRolesFromToken(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}