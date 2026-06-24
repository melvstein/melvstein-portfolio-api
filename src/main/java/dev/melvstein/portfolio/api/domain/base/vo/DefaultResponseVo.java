package dev.melvstein.portfolio.api.domain.base.vo;

import lombok.Builder;

@Builder
public record DefaultResponseVo(
        int code,
        String message
) implements BaseResponseVo {

    public DefaultResponseVo error(int code, String message) {
        return DefaultResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }
}
