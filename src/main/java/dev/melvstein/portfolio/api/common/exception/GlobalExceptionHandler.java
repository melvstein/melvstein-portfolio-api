package dev.melvstein.portfolio.api.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.melvstein.portfolio.api.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.base.vo.ErrorResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseVo> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife
                && ife.getTargetType().isEnum()) {

            String field = ife.getPath().getFirst().getFieldName();

            return ResponseEntity.badRequest()
                    .body(ErrorResponseVo.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid value for field: " + field)
                            .build());
        }

        return ResponseEntity.badRequest()
                .body(ErrorResponseVo.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid request payload")
                        .build());
    }
}
