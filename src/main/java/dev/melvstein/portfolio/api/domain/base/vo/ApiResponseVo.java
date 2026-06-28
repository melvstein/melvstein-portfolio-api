package dev.melvstein.portfolio.api.domain.base.vo;

import lombok.Builder;

@Builder
public record ApiResponseVo(
        int code,
        String message
) implements BaseResponseVo {

    public ApiResponseVo error(int code, String message) {
        return ApiResponseVo.builder()
                .code(code)
                .message(message)
                .build();
    }
}
