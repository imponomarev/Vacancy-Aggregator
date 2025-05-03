package com.example.vacancy_aggregator.exception;

import com.example.vacancy_aggregator.dto.error.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex);
    }


    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeign(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        // если статус не 4xx/5xx – трактуем как 502 Bad Gateway
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        log.warn("Feign error: {}", ex.contentUTF8());
        return build(status, ex);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleAny(Throwable ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus http,
                                                Throwable ex) {
        log.error("[{}] {}", ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(http.value())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();

        return ResponseEntity.status(http).body(body);
    }
}
