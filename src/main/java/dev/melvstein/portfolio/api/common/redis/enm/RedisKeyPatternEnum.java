package dev.melvstein.portfolio.api.common.redis.enm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPatternEnum {
    JWT_TOKEN("jwt-token:username:%s"),
    REFRESH_TOKEN("refresh-token:username:%s"),
    USER_BY_ID("user:id:%s"),
    USER_BY_USERNAME("user:username:%s"),
    USER_DTO_BY_ID("user-dto:id:%s"),
    USER_DTO_BY_USERNAME("user-dto:username:%s");

    private final String pattern;
}
