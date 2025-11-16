package com.myrumi.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증 유틸리티
 */
@Slf4j
@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds:3600}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity-in-seconds:604800}") long refreshTokenValidity) {
        
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidity * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidity * 1000;
    }
    
    /**
     * Access Token 생성
     */
    public String createAccessToken(String username, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 토큰에서 Authentication 객체 추출
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        
        Collection<? extends GrantedAuthority> authorities = 
            Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }
    
    /**
     * 토큰에서 사용자명 추출
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }
    /**
     * 토큰 유효성 검증
     */
public boolean validateToken(String token) {
    try {
        Jwts.parser()
            .setSigningKey(secretKey)
            .build() 
            .parseClaimsJws(token);
        return true;
    } catch (SecurityException | MalformedJwtException e) {
        log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        log.error("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        log.error("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
}

    /**
     * 토큰 파싱
    */
    private Claims parseClaims(String token) {
    try {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()  
                .parseClaimsJws(token)
                .getBody();
         } catch (ExpiredJwtException e) {
        return e.getClaims();
        }
    }
}