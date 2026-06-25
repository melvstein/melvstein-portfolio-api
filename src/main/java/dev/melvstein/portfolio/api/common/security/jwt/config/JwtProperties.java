package dev.melvstein.portfolio.api.common.security.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(

        String secret,
        Expiration expiration
) {

    public record Expiration(

            long accessToken,
            long refreshToken
    ) {}
}
