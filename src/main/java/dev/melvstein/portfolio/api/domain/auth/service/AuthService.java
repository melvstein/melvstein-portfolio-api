package dev.melvstein.portfolio.api.domain.auth.service;

import dev.melvstein.portfolio.api.common.exception.ApiException;
import dev.melvstein.portfolio.api.common.redis.service.RedisService;
import dev.melvstein.portfolio.api.common.security.jwt.enm.JwtTypeEnum;
import dev.melvstein.portfolio.api.common.security.jwt.service.JwtService;
import dev.melvstein.portfolio.api.domain.auth.converter.AuthConverter;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthLoginRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRefreshTokenRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthLoginResponseVo;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRefreshTokenResponseVo;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRegisterResponseVo;
import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService extends BaseService {

    private final UserRepository userRepository;
    private final AuthConverter authConverter;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisService redisService;

    @CacheEvict(value = "users-cache", allEntries = true)
    public AuthRegisterResponseVo register(AuthRegisterRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(ResponseCodeEnum.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(ResponseCodeEnum.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByContactNumber(request.contactNumber())) {
            throw new ApiException(ResponseCodeEnum.CONTACT_NUMBER_ALREADY_EXISTS);
        }

        User user = authConverter.toUserEntity(request);
        UserDto userDto = userConverter.toDto(userRepository.save(user));

        return AuthRegisterResponseVo.response(
                ResponseCodeEnum.SUCCESS,
                userDto
        );
    }

    public AuthLoginResponseVo login(AuthLoginRequestDto request) {
        Optional<User> cachedUser = redisService.getCachedUser(request.username());
        User user;

        if (cachedUser.isPresent()) {
            user = cachedUser.get();

            log.info("[login] - User found in cache. user: {}", user);
        } else {
            user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> {
                        log.error("[login] - User not found for username: {}", request.username());

                        return new ApiException(ResponseCodeEnum.USER_NOT_FOUND);
                    });

            redisService.cacheUser(user);
        }

        boolean isMatch = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!isMatch) {
            log.error("[login] - Passwords do not match.");

            throw new ApiException(ResponseCodeEnum.INVALID_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        AuthLoginResponseVo.Data data = AuthLoginResponseVo.Data.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return AuthLoginResponseVo.response(ResponseCodeEnum.SUCCESS, data);
    }

    public AuthRefreshTokenResponseVo refreshToken(AuthRefreshTokenRequestDto request) {
        JwtTypeEnum type = jwtService.extractType(request.refreshToken());

        if (!JwtTypeEnum.REFRESH.equals(type)) {
            log.error("[refreshToken] - Invalid refresh token. type: {}, request: {}", type, request);

            throw new ApiException(ResponseCodeEnum.INVALID_TOKEN, "Invalid refresh token");
        }

        String username = request.username();

        try {
            if (!jwtService.isTokenValid(request.refreshToken(), username)) {
                return AuthRefreshTokenResponseVo.error(
                        ResponseCodeEnum.INVALID_TOKEN.getCode(),
                        ResponseCodeEnum.INVALID_TOKEN.getMessage()
                );
            }
        } catch (SignatureException e) {
            log.error("[refreshToken] - Invalid token.");

            throw new ApiException(ResponseCodeEnum.INVALID_TOKEN);
        }

        String accessToken = jwtService.generateAccessToken(username);

        return AuthRefreshTokenResponseVo.response(ResponseCodeEnum.SUCCESS, accessToken);
    }
}
