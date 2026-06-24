package dev.melvstein.portfolio.api.domain.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.melvstein.portfolio.api.common.exception.ApiException;
import dev.melvstein.portfolio.api.domain.base.vo.BaseResponseVo;
import dev.melvstein.portfolio.api.common.enm.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public abstract class BaseController {

    protected <R extends BaseResponseVo> ResponseEntity<R> handleValidationError(
            BindingResult bindingResult,
            Object request,
            BiFunction<Integer, String, R> errorFactory
    ) {
        String message = Optional.ofNullable(bindingResult.getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse(ResponseCodeEnum.INVALID_REQUEST.getMessage());

        int code = ResponseCodeEnum.INVALID_REQUEST.getCode();

        log.error("handleValidationError - code: {}, message: {}, request: {}", code, message, request);

        return ResponseEntity.badRequest()
                .body(errorFactory.apply(code, message));
    }

    protected <T, R extends BaseResponseVo> ResponseEntity<R> handleResponse(
            T request,
            Function<T, R> serviceMethod,
            BiFunction<Integer, String, R> errorFactory
    ) throws JsonProcessingException {
        ResponseEntity<R> response;
        ObjectMapper mapper = new ObjectMapper();

        try {
            response = ResponseEntity.ok(serviceMethod.apply(request));
        } catch (DataIntegrityViolationException e) {
            log.error("handleResponse - DataIntegrityViolationException", e);

            response = ResponseEntity.badRequest()
                    .body(errorFactory.apply(
                            ResponseCodeEnum.DUPLICATE_ENTRY.getCode(),
                            ResponseCodeEnum.DUPLICATE_ENTRY.getMessage())
                    );
        } catch (ApiException e) {
            log.error("handleResponse - AppException", e);

            response = ResponseEntity.status(e.getHttpStatus())
                    .body(errorFactory.apply(
                            e.getCode(),
                            e.getMessage())
                    );
        } catch (Exception e) {
            log.error("handleResponse", e);

            response = ResponseEntity.internalServerError()
                    .body(errorFactory.apply(
                            ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(),
                            ResponseCodeEnum.INTERNAL_SERVER_ERROR.getMessage())
                    );
        }

        log.info("handleResponse - request: {}, response: {}", request, mapper.writeValueAsString(response));

        return response;
    }
}
