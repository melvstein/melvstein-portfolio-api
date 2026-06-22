package dev.melvstein.portfolio.api.user.dto;

import dev.melvstein.portfolio.api.common.validation.annotation.EnumConstraint;
import dev.melvstein.portfolio.api.user.enm.RoleEnum;
import dev.melvstein.portfolio.api.user.enm.StatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserCreateRequestDto(

        @NotNull(message = "Role is required")
        RoleEnum role,

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
        StatusEnum status
) {
}
