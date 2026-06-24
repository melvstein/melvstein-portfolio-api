package dev.melvstein.portfolio.api.domain.user.service;

import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import dev.melvstein.portfolio.api.domain.user.vo.UserResponseVo;
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
       //System.out.println("API_KEY " + apiKey);
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
