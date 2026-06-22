package dev.melvstein.portfolio.api.base.vo;

import lombok.Builder;

@Builder
public record ErrorResponseVo(
        int code,
        String message
) implements BaseResponseVo {
}
