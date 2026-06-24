package dev.melvstein.portfolio.api.common.redis.service;

import dev.melvstein.portfolio.api.common.redis.config.RedisProperties;
import dev.melvstein.portfolio.api.common.redis.enm.RedisKeyPatternEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    public void cacheUser(User user) {
        String key = String.format(
                RedisKeyPatternEnum.USER.getPattern(),
                user.getUsername()
        );

        redisTemplate.opsForValue().set(key, user, redisProperties.expiration().user());
    }

    public Optional<User> getCachedUser(String username) {
        String key = String.format(
                RedisKeyPatternEnum.USER.getPattern(),
                username
        );

        return Optional.ofNullable((User) redisTemplate.opsForValue().get(key));
    }
}
