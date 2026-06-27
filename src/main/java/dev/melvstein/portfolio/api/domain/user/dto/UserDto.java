package dev.melvstein.portfolio.api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.melvstein.portfolio.api.domain.user.enm.UserRoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.UserStatusEnum;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Builder
public record UserDto(

        Long id,
        UserRoleEnum role,
        String firstName,
        String middleName,
        String lastName,
        String username,
        String email,
        String contactNumber,
        UserStatusEnum status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date updatedAt
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
