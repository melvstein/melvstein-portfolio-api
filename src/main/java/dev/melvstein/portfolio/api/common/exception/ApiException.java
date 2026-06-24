package dev.melvstein.portfolio.api.common.exception;

import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiException extends RuntimeException {

    private final int code;
    private final HttpStatus httpStatus;

    public ApiException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ApiException(ResponseCodeEnum response, String message) {
        this(response.getCode(), message, response.getHttpStatus());
    }

    public ApiException(ResponseCodeEnum response, HttpStatus httpStatus) {
        this(response.getCode(), response.getMessage(), httpStatus);
    }

    public ApiException(ResponseCodeEnum response) {
        this(response.getCode(), response.getMessage(), response.getHttpStatus());
    }
}
