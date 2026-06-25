package dev.melvstein.portfolio.api.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuthRefreshTokenResponseVo(

        int code,
        String message,
        String accessToken
) implements BaseResponseVo {

    public static AuthRefreshTokenResponseVo error(int code, String message) {
        return AuthRefreshTokenResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static AuthRefreshTokenResponseVo response(ResponseCodeEnum response, String accessToken) {
        return AuthRefreshTokenResponseVo.builder()
                .code(response.getCode())
                .message(response.getMessage())
                .accessToken(accessToken)
                .build();
    }
}
