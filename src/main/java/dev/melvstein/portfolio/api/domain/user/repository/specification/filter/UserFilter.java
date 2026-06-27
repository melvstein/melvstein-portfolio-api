package dev.melvstein.portfolio.api.domain.user.repository.specification.filter;

import dev.melvstein.portfolio.api.domain.user.enm.UserRoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.UserStatusEnum;

import java.time.LocalDateTime;

public record UserFilter(

        UserRoleEnum role,
        UserStatusEnum status,
        String username,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo
) {
}
