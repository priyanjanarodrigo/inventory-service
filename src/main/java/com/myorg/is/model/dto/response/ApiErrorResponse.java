package com.myorg.is.model.dto.response;

import lombok.Builder;

/**
 * ApiErrorResponse is a DTO representing an error response from the API.
 *
 * @param path       The request path that caused the error
 * @param httpStatus The HTTP status code
 * @param message    The error message.
 */
@Builder
public record ApiErrorResponse(String path, Integer httpStatus, String message) {

}
