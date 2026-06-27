package dev.melvstein.portfolio.api.domain.auth.dto;

import dev.melvstein.portfolio.api.domain.user.enm.UserRoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.UserStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AuthRegisterRequestDto(

        @NotNull(message = "Role is required")
        UserRoleEnum role,

        @NotBlank(message = "First name is required")
        String firstName,

        String middleName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        @Email(message = "Invalid email address")
        String email,

        @Pattern(
                regexp = "^[0-9]{11}$",
                message = "Contact number must contain 11 digits"
        )
        String contactNumber,

        @NotNull(message = "Status is required")
        UserStatusEnum status) {
}
