package dev.melvstein.portfolio.api.domain.user.dto;

import dev.melvstein.portfolio.api.domain.user.enm.UserRoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.UserStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserUpdateRequestDto(

        UserRoleEnum role,
        String firstName,
        String middleName,
        String lastName,
        String username,
        String password,

        @Email(message = "Invalid email address")
        String email,

        @Pattern(
                regexp = "^[0-9]{11}$",
                message = "Contact number must contain 11 digits"
        )
        String contactNumber,

        UserStatusEnum status
) {
}
