package com.myorg.is.model.dto.response;

import com.myorg.is.util.enums.Status;

/**
 * MetaData is a DTO representing metadata information for API responses.
 *
 * @param status The status of the response (e.g., "success", "error").
 */
public record MetaData(Status status) {

}