package com.example.vacancy_aggregator.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

/**
 * Унифицированная модель ответа об ошибке.
 * Содержит HTTP-статус, читаемое сообщение и метку времени.
 */
@Getter
@Builder
public class ErrorResponse {

    /**
     * HTTP‑код, который вернётся клиенту.
     */
    private final int status;

    /**
     * Читаемое сообщение для клиента.
     */
    private final String message;

    /**
     * Момент формирования ответа.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final OffsetDateTime timestamp;
}
