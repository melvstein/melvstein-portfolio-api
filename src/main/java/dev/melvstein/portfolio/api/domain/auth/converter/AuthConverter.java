package dev.melvstein.portfolio.api.domain.auth.converter;

import dev.melvstein.portfolio.api.common.Utils;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConverter {

    private final PasswordEncoder passwordEncoder;

    public User toUserEntity(AuthRegisterRequestDto request) {
        if (request == null) {
            return null;
        }

        String password = passwordEncoder.encode(request.password());

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
