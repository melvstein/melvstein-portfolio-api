package dev.melvstein.portfolio.api.common.redis.service;

import dev.melvstein.portfolio.api.common.redis.config.RedisProperties;
import dev.melvstein.portfolio.api.common.redis.enm.RedisKeyPatternEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    public void cache(RedisKeyPatternEnum pattern, String key, Object value) {
        String cacheKey = pattern.getPattern().formatted(key);
        redisTemplate.opsForValue().set(cacheKey, value, redisProperties.expiration().defaultAll());
    }

    public void cache(RedisKeyPatternEnum pattern, String key, Object value, Duration expiration) {
        String cacheKey = pattern.getPattern().formatted(key);
        redisTemplate.opsForValue().set(cacheKey, value, expiration);
    }

    public <T> Optional<T> get(RedisKeyPatternEnum pattern, String key, Class<T> clazz) {
        String cacheKey = pattern.getPattern().formatted(key);
        return Optional.ofNullable(clazz.cast(redisTemplate.opsForValue().get(cacheKey)));
    }

    public void cacheUser(User user) {
        cache(RedisKeyPatternEnum.USER, user.getUsername(), user, redisProperties.expiration().user());
    }

    public Optional<User> getCachedUser(String username) {
        return get(RedisKeyPatternEnum.USER, username, User.class);
    }
}
