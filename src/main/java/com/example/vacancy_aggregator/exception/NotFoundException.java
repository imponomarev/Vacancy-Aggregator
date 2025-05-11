package com.example.vacancy_aggregator.exception;

/**
 * Выбрасывается, когда ресурс не найден.
 * Используется контроллером для генерации HTTP 404 Not Found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
