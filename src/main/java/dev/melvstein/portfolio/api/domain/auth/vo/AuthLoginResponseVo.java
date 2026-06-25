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
        Data data
) implements BaseResponseVo {

    public static AuthLoginResponseVo error(int code, String message) {
        return AuthLoginResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static AuthLoginResponseVo response(ResponseCodeEnum responseCodeEnum, Data data) {
        return AuthLoginResponseVo.builder()
                .code(responseCodeEnum.getCode())
                .message(responseCodeEnum.getMessage())
                .data(data)
                .build();
    }

    @Builder
    public record Data(

            String accessToken,
            String refreshToken
    ) {

    }
}
