package dev.melvstein.portfolio.api.domain.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.entity.User;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UserResponseVo(

        int code,
        String message,
        UserDto data
) implements BaseResponseVo {

    public static UserResponseVo error(int code, String message) {
        return UserResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record GetAll(

            int code,
            String message,
            List<UserDto> data,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last
    ) implements Serializable {

    }
}
