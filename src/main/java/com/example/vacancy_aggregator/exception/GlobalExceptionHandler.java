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

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * Перехватывает различные типы исключений и формирует унифицированный
 * {@link ErrorResponse} с корректным HTTP-статусом и сообщением.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Обрабатывает ошибки валидации и некорректные аргументы.
     * Возвращает 400 Bad Request.
     */
    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Обрабатывает ошибки доступа (нет прав).
     * Возвращает 403 Forbidden.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, ex);
    }

    /**
     * Обрабатывает ошибки аутентификации.
     * Возвращает 401 Unauthorized.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex);
    }

    /**
     * Обрабатывает пользовательское исключение «не найдено».
     * Возвращает 404 Not Found.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Обрабатывает не поддерживаемые HTTP-методы.
     * Возвращает 405 Method Not Allowed.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex);
    }

    /**
     * Обрабатывает ошибки Feign-клиентов.
     * Статус ответа соответствует коду из FeignException или 502 Bad Gateway.
     */
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

    /**
     * Обрабатывает все остальные необработанные исключения.
     * Возвращает 500 Internal Server Error.
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleAny(Throwable ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /**
     * Вспомогательный метод для построения ответа с телом {@link ErrorResponse}.
     *
     * @param http статус HTTP-ответа
     * @param ex   исключение
     * @return построенный ResponseEntity
     */
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
