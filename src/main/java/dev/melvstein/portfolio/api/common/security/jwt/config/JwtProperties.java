package dev.melvstein.portfolio.api.common.security.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(

        String secret,
        long expiration
) {
}
