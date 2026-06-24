package dev.melvstein.portfolio.api.domain.auth.service;

import dev.melvstein.portfolio.api.domain.auth.converter.AuthConverter;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRegisterResponseVo;
import dev.melvstein.portfolio.api.domain.base.service.BaseService;
import dev.melvstein.portfolio.api.domain.user.converter.UserConverter;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService {

    private final UserRepository userRepository;

    AuthService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey
    ) {
        this.userRepository = userRepository;
        this.apiKey = apiKey;
    }

    public AuthRegisterResponseVo register(AuthRegisterRequestDto request) {
        User user = AuthConverter.toUserEntity(request);
        UserDto userDto = UserConverter.toDto(userRepository.save(user));

        return AuthRegisterResponseVo.response(
                ResponseCodeEnum.SUCCESS,
                userDto
        );
    }
}
