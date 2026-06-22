package dev.melvstein.portfolio.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.melvstein.portfolio.api.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.user.dto.UserDto;
import lombok.Builder;

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
}
