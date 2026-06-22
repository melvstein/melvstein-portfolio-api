package dev.melvstein.portfolio.api.user.service;

import dev.melvstein.portfolio.api.user.converter.UserConverter;
import dev.melvstein.portfolio.api.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.user.dto.UserDto;
import dev.melvstein.portfolio.api.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.user.entity.User;
import dev.melvstein.portfolio.api.user.repository.UserRepository;
import dev.melvstein.portfolio.api.user.vo.UserResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseVo createUser(UserCreateRequestDto request) {
        User user = UserConverter.toEntity(request);
        UserDto userDto = UserConverter.toDto(userRepository.save(user));

        return UserResponseVo.builder()
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .data(userDto)
                .build();
    }
}
