package dev.melvstein.portfolio.api.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.auth.service.AuthService;
import dev.melvstein.portfolio.api.domain.auth.vo.AuthRegisterResponseVo;
import dev.melvstein.portfolio.api.domain.base.controller.BaseController;
import dev.melvstein.portfolio.api.domain.user.enm.ResponseCodeEnum;
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

        if (!authService.verifyApiKey(apiKey)) {
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
}
