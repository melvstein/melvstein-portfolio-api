package dev.melvstein.portfolio.api.user.service;

import dev.melvstein.portfolio.api.base.service.BaseService;
import dev.melvstein.portfolio.api.user.converter.UserConverter;
import dev.melvstein.portfolio.api.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.user.dto.UserDto;
import dev.melvstein.portfolio.api.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.user.entity.User;
import dev.melvstein.portfolio.api.user.repository.UserRepository;
import dev.melvstein.portfolio.api.user.vo.UserResponseVo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;

    UserService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey
    ) {
        this.userRepository = userRepository;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
       System.out.println("API_KEY " + apiKey);
    }

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
