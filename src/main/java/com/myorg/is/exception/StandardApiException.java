package com.myorg.is.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * StandardApiException is a custom exception class that extends RuntimeException and implements Serializable.
 * It is designed to represent API exceptions with a specific HTTP status and message.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardApiException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = -7826109728512120138L;

    private final HttpStatus httpStatus;

    private final String message;

    public StandardApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
