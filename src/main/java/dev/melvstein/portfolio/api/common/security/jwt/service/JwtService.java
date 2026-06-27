package dev.melvstein.portfolio.api.common.security.jwt.service;

import dev.melvstein.portfolio.api.common.security.jwt.config.JwtProperties;
import dev.melvstein.portfolio.api.common.security.jwt.enm.JwtTypeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    }

    public String tokenBuilder(JwtTypeEnum type, String username, long expiration) {
        return Jwts.builder()
                .subject(username)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(
                        secretKey,
                        Jwts.SIG.HS256
                )
                .compact();
    }

    public String generateAccessToken(String username) {
        return tokenBuilder(
                JwtTypeEnum.ACCESS,
                username,
                jwtProperties.expiration().accessToken()
        );
    }

    public String generateRefreshToken(String username) {
        return tokenBuilder(
                JwtTypeEnum.REFRESH,
                username,
                jwtProperties.expiration().refreshToken()
        );
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public JwtTypeEnum extractType(String token) {
        return JwtTypeEnum.valueOf(extractClaims(token).get("type", String.class));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}
