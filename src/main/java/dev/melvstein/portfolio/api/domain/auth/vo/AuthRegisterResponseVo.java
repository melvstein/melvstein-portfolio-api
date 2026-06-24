package dev.melvstein.portfolio.api.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.domain.user.dto.UserDto;
import dev.melvstein.portfolio.api.domain.user.enm.ResponseCodeEnum;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuthRegisterResponseVo(
        int code,
        String message,
        UserDto data
) implements BaseResponseVo {

    public static AuthRegisterResponseVo error(int code, String message) {
        return AuthRegisterResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static AuthRegisterResponseVo response(ResponseCodeEnum responseCodeEnum, UserDto data) {
        return AuthRegisterResponseVo.builder()
                .code(responseCodeEnum.getCode())
                .message(responseCodeEnum.getMessage())
                .data(data)
                .build();
    }
}
