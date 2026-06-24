package dev.melvstein.portfolio.api.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuthLoginResponseVo(

        int code,
        String message,
        String token
) implements BaseResponseVo {

    public static AuthLoginResponseVo error(int code, String message) {
        return AuthLoginResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static AuthLoginResponseVo response(ResponseCodeEnum responseCodeEnum, String token) {
        return AuthLoginResponseVo.builder()
                .code(responseCodeEnum.getCode())
                .message(responseCodeEnum.getMessage())
                .token(token)
                .build();
    }
}
