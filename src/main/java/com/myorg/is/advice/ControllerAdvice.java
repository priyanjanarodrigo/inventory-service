package com.myorg.is.advice;

import com.myorg.is.exception.StandardApiException;
import com.myorg.is.model.dto.response.ApiErrorResponse;
import com.myorg.is.model.dto.response.MetaData;
import com.myorg.is.model.dto.response.StandardApiResponse;
import com.myorg.is.util.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * ControllerAdvice is a global exception handler that provides centralized handling of exceptions thrown by
 * controllers. It handles validation errors and custom API exceptions, returning standardized error
 * responses to the client.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    /**
     * Handles validation errors for request bodies.
     *
     * @param methodArgumentNotValidException the exception thrown when validation fails
     * @return a map of field names to error messages
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        log.warn("Validation failed: {}", methodArgumentNotValidException.getMessage());
        return methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));
    }

    /**
     * Handles validation errors for request parameters and path variables.
     *
     * @param ex the exception thrown when constraint violations occur
     * @return a map of property paths to error messages
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return ex.getConstraintViolations()
                .stream()
                .collect(toMap(
                        violation -> {
                            String path = violation.getPropertyPath().toString();
                            int lastDot = path.lastIndexOf('.');
                            return (lastDot != -1) ? path.substring(lastDot + 1) : path;
                        },
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));
    }

    /**
     * Handles StandardApiException and constructs a standardized error response.
     *
     * @param standardApiException the exception thrown
     * @param httpServletRequest   the HTTP servlet request
     * @return a ResponseEntity containing the standardized error response
     */
    @ExceptionHandler(StandardApiException.class)
    public ResponseEntity<StandardApiResponse> handleStandardApiException(
            StandardApiException standardApiException, HttpServletRequest httpServletRequest) {

        return new ResponseEntity<>(
                new StandardApiResponse(
                        new MetaData(Status.ERROR),
                        new ApiErrorResponse(httpServletRequest.getRequestURI(),
                                standardApiException.getHttpStatus().value(), standardApiException.getMessage())),
                standardApiException.getHttpStatus()
        );
    }
}
