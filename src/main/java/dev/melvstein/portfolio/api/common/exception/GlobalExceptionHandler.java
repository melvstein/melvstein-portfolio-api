package dev.melvstein.portfolio.api.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.domain.base.vo.DefaultResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseVo> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife
                && ife.getTargetType().isEnum()) {

            String fieldName = ife.getPath().stream()
                    .findFirst()
                    .map(JsonMappingException.Reference::getFieldName)
                    .orElse("field");

            String allowedValues = Arrays.stream(
                            ife.getTargetType().getEnumConstants()
                    )
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.badRequest()
                    .body(DefaultResponseVo.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(String.format(
                                    "%s must be one of: %s",
                                    fieldName,
                                    allowedValues
                            ))
                            .build());
        }

        return ResponseEntity.badRequest()
                .body(DefaultResponseVo.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid request payload")
                        .build());
    }
}
