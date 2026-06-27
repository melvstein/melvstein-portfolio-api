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
    INVALID_PASSWORD(8, "Invalid Password", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(9, "Invalid Token", HttpStatus.UNAUTHORIZED),
    USERNAME_ALREADY_EXISTS(10, "Username Already Exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(11, "Email Already Exists", HttpStatus.CONFLICT),
    CONTACT_NUMBER_ALREADY_EXISTS(12, "Contact Number Already Exists", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
