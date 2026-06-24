package dev.melvstein.portfolio.api.common.redis.enm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPatternEnum {
    JWT_TOKEN("jwt-token:username:%s"),
    REFRESH_TOKEN("refresh-token:username:%s"),
    USER("user:username:%s");

    private final String pattern;
}
