package dev.melvstein.portfolio.api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.melvstein.portfolio.api.domain.user.enm.RoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.StatusEnum;
import lombok.Builder;

import java.util.Date;

@Builder
public record UserDto(

        Long id,
        RoleEnum role,
        String firstName,
        String middleName,
        String lastName,
        String username,
        String email,
        String contactNumber,
        StatusEnum status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date updatedAt
) {
}
