package dev.melvstein.portfolio.api.domain.user.service;

import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.common.exception.ApiException;
import dev.melvstein.portfolio.api.common.redis.enm.RedisKeyPatternEnum;
import dev.melvstein.portfolio.api.common.redis.service.RedisService;
import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import dev.melvstein.portfolio.api.domain.user.repository.specification.UserSpecification;
import dev.melvstein.portfolio.api.domain.user.repository.specification.filter.UserFilter;
import dev.melvstein.portfolio.api.domain.user.vo.UserResponseVo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RedisService redisService;

    UserService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey, UserConverter userConverter, RedisService redisService
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.redisService = redisService;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
       //System.out.println("API_KEY " + apiKey);
    }

    @Cacheable(
            cacheNames = "users-cache",
            key = "#filter.toString() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort"
    )
    public UserResponseVo.GetAll getAllUsers(UserFilter filter, Pageable pageable) {
        Page<User> users = userRepository.findAll(UserSpecification.filter(filter), pageable);

        List<UserDto> userDtos = users.getContent()
                .stream()
                .map(userConverter::toDto)
                .toList();

        return UserResponseVo.GetAll.builder()
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .data(userDtos)
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .first(users.isFirst())
                .last(users.isLast())
                .build();
    }

    public UserResponseVo.GetOne getUserById(Long userId) {
        RedisKeyPatternEnum redisKeyPattern = RedisKeyPatternEnum.USER_DTO_BY_ID;

        UserDto userDto = redisService.get(
                redisKeyPattern,
                String.valueOf(userId),
                UserDto.class
        )
                .map((cachedUserDto) -> {
                    log.info("[getUserById] UserDto found in cache for userId: {}, userDto: {}", userId, cachedUserDto);

                    return cachedUserDto;
                })
                .orElseGet(() -> {
            UserDto findUser = userRepository.findById(userId)
                    .map(userConverter::toDto)
                    .orElseThrow(() -> new ApiException(ResponseCodeEnum.USER_NOT_FOUND));

            redisService.cache(
                    redisKeyPattern,
                    findUser.id().toString(),
                    findUser
            );

            return findUser;
        });

        return UserResponseVo.GetOne.builder()
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .data(userDto)
                .build();
    }
}
