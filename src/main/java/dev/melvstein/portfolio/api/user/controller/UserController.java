package dev.melvstein.portfolio.api.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.base.controller.BaseController;
import dev.melvstein.portfolio.api.user.dto.UserCreateRequestDto;
import dev.melvstein.portfolio.api.user.dto.UserDto;
import dev.melvstein.portfolio.api.user.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.user.service.UserService;
import dev.melvstein.portfolio.api.user.vo.UserResponseVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseVo> createUser(
            @Valid @RequestBody UserCreateRequestDto request,
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if  (bindingResult.hasErrors()) {
            return handleValidationError(
                    bindingResult,
                    request,
                    UserResponseVo::error
            );
        }

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            UserResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Header X-API-Key is required"
                            )
                    );
        }

        if (!userService.verifyApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            UserResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Invalid header X-API-Key"
                            )
                    );
        }

        return handleResponse(request, userService::createUser, UserResponseVo::error);
    }
}
