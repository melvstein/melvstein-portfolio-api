package dev.melvstein.portfolio.api.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.common.Utils;
import dev.melvstein.portfolio.api.common.security.service.SecurityService;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthLoginRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRefreshTokenRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.auth.service.AuthService;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthLoginResponseVo;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRefreshTokenResponseVo;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRegisterResponseVo;
import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {

    private final SecurityService securityService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponseVo> register(
            @Valid @RequestBody AuthRegisterRequestDto request,
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if  (bindingResult.hasErrors()) {
            return handleValidationError(
                    bindingResult,
                    request,
                    AuthRegisterResponseVo::error
            );
        }

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthRegisterResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Header X-API-Key is required"
                            )
                    );
        }

        if (securityService.verifyApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthRegisterResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Invalid header X-API-Key"
                            )
                    );
        }

        return handleResponse(request, authService::register, AuthRegisterResponseVo::error);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseVo> login(
            @Valid @RequestBody AuthLoginRequestDto request,
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if  (bindingResult.hasErrors()) {
            return handleValidationError(
                    bindingResult,
                    request,
                    AuthLoginResponseVo::error
            );
        }

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthLoginResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Header X-API-Key is required"
                            )
                    );
        }

        if (securityService.verifyApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthLoginResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Invalid header X-API-Key"
                            )
                    );
        }

        return handleResponse(request, authService::login, AuthLoginResponseVo::error);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthRefreshTokenResponseVo> refreshToken(
            @Valid @RequestBody AuthRefreshTokenRequestDto request,
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if  (bindingResult.hasErrors()) {
            return handleValidationError(
                    bindingResult,
                    request,
                    AuthRefreshTokenResponseVo::error
            );
        }

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthRefreshTokenResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Header X-API-Key is required"
                            )
                    );
        }

        if (securityService.verifyApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            AuthRefreshTokenResponseVo.error(
                                    ResponseCodeEnum.UNAUTHORIZED.getCode(),
                                    "Invalid header X-API-Key"
                            )
                    );
        }

        return handleResponse(request, authService::refreshToken, AuthRefreshTokenResponseVo::error);
    }
}
