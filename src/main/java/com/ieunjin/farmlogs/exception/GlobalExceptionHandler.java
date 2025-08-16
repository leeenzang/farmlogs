package com.ieunjin.farmlogs.exception;

import com.ieunjin.farmlogs.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();

        log.warn("[{}] {}", code.name(), code.getMessage());

        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.of(code));
    }
}