package dev.melvstein.portfolio.api.common.redis.dto;

import dev.melvstein.portfolio.api.common.redis.enm.RedisKeyPatternEnum;
import lombok.Builder;

@Builder
public record RedisParamDto(

        RedisKeyPatternEnum pattern,
        String key,
        Object value
) {
}
