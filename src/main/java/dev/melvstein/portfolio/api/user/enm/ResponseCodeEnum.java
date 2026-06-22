package dev.melvstein.portfolio.api.user.enm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    SUCCESS(0, "Success"),
    INTERNAL_SERVER_ERROR(1, "Internal Server Error"),
    INVALID_REQUEST(2, "Invalid Request"),
    DUPLICATE_ENTRY(3, "Duplicate Entry");

    private final int code;
    private final String message;
}
