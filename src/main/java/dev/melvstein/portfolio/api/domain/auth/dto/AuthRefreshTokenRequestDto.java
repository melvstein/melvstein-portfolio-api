package dev.melvstein.portfolio.api.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRefreshTokenRequestDto(

        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "refreshToken is required")
        String refreshToken
) {
}
