package dev.melvstein.portfolio.api.data;

import dev.melvstein.portfolio.api.domain.auth.dto.AuthLoginRequestDto;
import dev.melvstein.portfolio.api.domain.auth.dto.AuthRegisterRequestDto;
import dev.melvstein.portfolio.api.domain.user.enm.UserRoleEnum;
import dev.melvstein.portfolio.api.domain.user.enm.UserStatusEnum;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserData {

    private final PasswordEncoder passwordEncoder;

    public User get() {
        return User.builder()
                .role(UserRoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password(passwordEncoder.encode("password"))
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(UserStatusEnum.ACTIVE)
                .build();
    }

    public static AuthRegisterRequestDto registerRequest() {
        return AuthRegisterRequestDto.builder()
                .role(UserRoleEnum.ADMIN)
                .firstName("Melvin Justine")
                .middleName("Lisay")
                .lastName("Bayogo")
                .username("melvstein")
                .password("password")
                .email("melvinbayogo@gmail.com")
                .contactNumber("09560627650")
                .status(UserStatusEnum.ACTIVE)
                .build();
    }

    public static AuthLoginRequestDto loginRequest() {
        return AuthLoginRequestDto.builder()
                .username("melvstein")
                .password("password")
                .build();
    }
}
