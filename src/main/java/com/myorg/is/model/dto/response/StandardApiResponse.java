package com.myorg.is.model.dto.response;

/**
 * StandardApiResponse is a DTO representing a standard API response structure.
 *
 * @param metaData Metadata about the response
 * @param data     The actual data being returned
 */
public record StandardApiResponse(MetaData metaData, Object data) {

}
