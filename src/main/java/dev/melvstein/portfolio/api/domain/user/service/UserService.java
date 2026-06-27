package dev.melvstein.portfolio.api.domain.user.service;

import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    UserService(
            UserRepository userRepository,
            @Value("${app.api-key}") String apiKey, UserConverter userConverter
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
       //System.out.println("API_KEY " + apiKey);
    }

    public UserResponseVo.GetAll getAllUsers(UserFilter filter, Pageable pageable) {
        Page<User> users = userRepository.findAll(UserSpecification.filter(filter), pageable);

        log.info("melvstein pageUsers={}", users.getContent());

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
}
