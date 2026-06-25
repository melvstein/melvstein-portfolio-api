package dev.melvstein.portfolio.api.auth.service;

import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.common.exception.ApiException;
import dev.melvstein.portfolio.api.common.redis.service.RedisService;
import dev.melvstein.portfolio.api.common.security.jwt.service.JwtService;
import dev.melvstein.portfolio.api.domain.auth.converter.AuthConverter;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthLoginRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.auth.service.AuthService;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthLoginResponseVo;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRegisterResponseVo;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.enm.RoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.StatusEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthConverter authConverter;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterSuccessfully() {

        // Arrange
        AuthRegisterRequestDto request = AuthRegisterRequestDto.builder()
                .role(RoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password("password")
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(StatusEnum.ACTIVE)
                .build();

        User user = User.builder()
                .role(request.role())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .username(request.username())
                .password("encoded-password")
                .email(request.email())
                .contactNumber(request.contactNumber())
                .status(request.status())
                .build();

        User savedUser = User.builder()
                .id(1L)
                .role(request.role())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .username(request.username())
                .password("encoded-password")
                .email(request.email())
                .contactNumber(request.contactNumber())
                .status(request.status())
                .build();

        UserDto userDto = UserDto.builder()
                .role(request.role())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .status(request.status())
                .build();

        when(authConverter.toUserEntity(request))
                .thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(savedUser);

        when(userConverter.toDto(savedUser))
                .thenReturn(userDto);

        // Act
        AuthRegisterResponseVo response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.code());
        assertEquals(ResponseCodeEnum.SUCCESS.getMessage(), response.message());
        assertNotNull(response.data());

        verify(authConverter).toUserEntity(request);
        verify(userRepository).save(user);
        verify(userConverter).toDto(savedUser);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        // Arrange
        AuthRegisterRequestDto request = AuthRegisterRequestDto.builder()
                .role(RoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password("password")
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(StatusEnum.ACTIVE)
                .build();

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(true);

        // Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals(
                ResponseCodeEnum.USER_ALREADY_EXISTS.getCode(),
                exception.getCode()
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        AuthRegisterRequestDto request = AuthRegisterRequestDto.builder()
                .role(RoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password("password")
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(StatusEnum.ACTIVE)
                .build();

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        // Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals(
                ResponseCodeEnum.EMAIL_ALREADY_EXISTS.getCode(),
                exception.getCode()
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenContactNumberAlreadyExists() {

        // Arrange
        AuthRegisterRequestDto request = AuthRegisterRequestDto.builder()
                .role(RoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password("password")
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(StatusEnum.ACTIVE)
                .build();

        when(userRepository.existsByContactNumber(request.contactNumber()))
                .thenReturn(true);

        // Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> authService.register(request)
        );

        assertEquals(
                ResponseCodeEnum.CONTACT_NUMBER_ALREADY_EXISTS.getCode(),
                exception.getCode()
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldLoginSuccessfullyWhenUserExistsInCache() {

        // Arrange
        AuthLoginRequestDto request = AuthLoginRequestDto.builder()
                .username("melvstein")
                .password("password")
                .build();

        User user = User.builder()
                .username("melvstein")
                .password("encoded-password")
                .build();

        when(redisService.getCachedUser(request.username()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(true);

        when(jwtService.generateAccessToken(user.getUsername()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user.getUsername()))
                .thenReturn("refresh-token");

        // Act
        AuthLoginResponseVo response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.data().accessToken());
        assertEquals("refresh-token", response.data().refreshToken());

        verify(userRepository, never())
                .findByUsername(anyString());
    }

    @Test
    void shouldLoginSuccessfullyWhenUserExistsInDb() {

        // Arrange
        AuthLoginRequestDto request = AuthLoginRequestDto.builder()
                .username("melvstein")
                .password("password")
                .build();

        User user = User.builder()
                .username("melvstein")
                .password("encoded-password")
                .build();

        when(userRepository.findByUsername(request.username()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(true);

        when(jwtService.generateAccessToken(user.getUsername()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user.getUsername()))
                .thenReturn("refresh-token");

        // Act
        AuthLoginResponseVo response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.data().accessToken());
        assertEquals("refresh-token", response.data().refreshToken());

        verify(userRepository)
                .findByUsername(request.username());
    }
}
