package dev.melvstein.portfolio.api.common.enm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    SUCCESS(0, "Success", HttpStatus.OK),
    INTERNAL_SERVER_ERROR(1, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(2, "Invalid Request", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTRY(3, "Duplicate Entry", HttpStatus.CONFLICT),
    INVALID_PARAMETER(4, "Invalid Parameter", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(5, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(6, "Invalid Credentials", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(7, "User Not Found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(8, "Invalid Password", HttpStatus.UNAUTHORIZED),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
