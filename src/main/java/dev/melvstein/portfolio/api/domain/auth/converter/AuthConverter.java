package dev.melvstein.portfolio.api.domain.auth.converter;

import dev.melvstein.portfolio.api.common.Utils;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;

public class AuthConverter {

    public static User toUserEntity(AuthRegisterRequestDto request) {
        if (request == null) {
            return null;
        }

        String password = Utils.bCryptPasswordEncoder().encode(request.password());

        return User.builder()
                .role(request.role())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .username(request.username())
                .password(password)
                .email(request.email())
                .contactNumber(request.contactNumber())
                .status(request.status())
                .build();
    }
}
