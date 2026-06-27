package dev.melvstein.portfolio.api.domain.user.service;

import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.common.exception.ApiException;
import dev.melvstein.portfolio.api.common.redis.enm.RedisKeyPatternEnum;
import dev.melvstein.portfolio.api.common.redis.service.RedisService;
import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.dto.UserUpdateRequestDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    UserService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey, UserConverter userConverter, RedisService redisService, PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
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

    public void evictUserCache(User user) {
        if (redisService.evict(RedisKeyPatternEnum.USER_BY_ID, String.valueOf(user.getId()))) {
            log.info("[evictUserCache] User found in cache for userId: {}", user.getId());
        }

        if (redisService.evict(RedisKeyPatternEnum.USER_BY_USERNAME, user.getUsername())) {
            log.info("[evictUserCache] User found in cache for username: {}", user.getUsername());
        }

        if (redisService.evict(RedisKeyPatternEnum.USER_DTO_BY_ID, String.valueOf(user.getId()))) {
            log.info("[evictUserCache] UserDto found in cache for userId: {}", user.getId());
        }
    }

    @Transactional
    public UserResponseVo.Update updateUserById(Long userId, UserUpdateRequestDto request) {
        if (request == null) {
            throw new ApiException(ResponseCodeEnum.INVALID_REQUEST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ResponseCodeEnum.USER_NOT_FOUND));

        if (request.role() != null) {
            user.setRole(request.role());
        }

        if (request.firstName() != null && !request.firstName().isBlank()) {
            user.setFirstName(request.firstName());
        }

        if (request.middleName() != null && !request.middleName().isBlank()) {
            user.setMiddleName(request.middleName());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            user.setLastName(request.lastName());
        }

        if (request.username() != null && !request.username().isBlank()
                && !user.getUsername().equals(request.username())
        ) {
            if (userRepository.existsByUsername(request.username())) {
                throw new ApiException(ResponseCodeEnum.USERNAME_ALREADY_EXISTS);
            }

            user.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isBlank()) {
            String password = passwordEncoder.encode(request.password());
            user.setPassword(password);
        }

        if (request.email() != null && !request.email().isBlank()
                && !user.getEmail().equals(request.email())
        ) {
            if (userRepository.existsByEmail(request.email())) {
                throw new ApiException(ResponseCodeEnum.EMAIL_ALREADY_EXISTS);
            }

            user.setEmail(request.email());
        }

        if (request.contactNumber() != null && !request.contactNumber().isBlank()
                && !user.getContactNumber().equals(request.contactNumber())
        ) {
            if  (userRepository.existsByContactNumber(request.contactNumber())) {
                throw new ApiException(ResponseCodeEnum.CONTACT_NUMBER_ALREADY_EXISTS);
            }

            user.setContactNumber(request.contactNumber());
        }

        if (request.status() != null) {
            user.setStatus(request.status());
        }

        User savedUser = userRepository.save(user);
        evictUserCache(savedUser);

        return UserResponseVo.Update.builder()
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .data(userConverter.toDto(savedUser))
                .build();
    }

    public UserResponseVo.Delete deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ResponseCodeEnum.USER_NOT_FOUND));

        userRepository.deleteById(user.getId());

        evictUserCache(user);

        return UserResponseVo.Delete.builder()
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .build();
    }
}
