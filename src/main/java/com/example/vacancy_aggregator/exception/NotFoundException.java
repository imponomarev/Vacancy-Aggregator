package com.example.vacancy_aggregator.exception;

/**
 * Унифицированное «не найдено».
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
