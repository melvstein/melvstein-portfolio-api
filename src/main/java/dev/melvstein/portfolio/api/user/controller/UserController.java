package dev.melvstein.portfolio.api.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.base.controller.BaseController;
import dev.melvstein.portfolio.api.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.user.dto.UserDto;
import dev.melvstein.portfolio.api.user.service.UserService;
import dev.melvstein.portfolio.api.user.vo.UserResponseVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseVo> createUser(
            @Valid @RequestBody UserCreateRequestDto request,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if  (bindingResult.hasErrors()) {
            return handleValidationError(
                    bindingResult,
                    request,
                    UserResponseVo::error
            );
        }

        return handleResponse(request, userService::createUser, UserResponseVo::error);
    }
}
