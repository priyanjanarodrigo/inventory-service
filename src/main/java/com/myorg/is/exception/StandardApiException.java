package com.myorg.is.exception;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class for standard API errors.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardApiException extends RuntimeException implements Serializable {

  @Serial
  private static final long serialVersionUID = -7826109728512120138L;

  private HttpStatus httpStatus;

  private String message;

  public StandardApiException(String message) {
    super(message);
  }

  public StandardApiException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
