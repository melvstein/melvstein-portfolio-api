package dev.melvstein.portfolio.api.common.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "redis")
public record RedisProperties(

    Expiration expiration
) {

    public record Expiration(

            Duration defaultAll,
            Duration refreshToken,
            Duration user,
            Duration jwtToken
    ) {

    }
}
