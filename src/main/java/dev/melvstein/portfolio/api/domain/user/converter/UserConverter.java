package dev.melvstein.portfolio.api.domain.user.converter;

import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserConverter {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .contactNumber(user.getContactNumber())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt() != null ? Date.from(user.getCreatedAt()) : null)
                .updatedAt(user.getUpdatedAt() != null ? Date.from(user.getUpdatedAt()) : null)
                .build();
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .id(dto.id())
                .role(dto.role())
                .firstName(dto.firstName())
                .middleName(dto.middleName())
                .lastName(dto.lastName())
                .username(dto.username())
                .email(dto.email())
                .contactNumber(dto.contactNumber())
                .status(dto.status())
                .createdAt(dto.createdAt() != null ? dto.createdAt().toInstant() : null)
                .updatedAt(dto.updatedAt() != null ? dto.updatedAt().toInstant() : null)
                .build();
    }
}
