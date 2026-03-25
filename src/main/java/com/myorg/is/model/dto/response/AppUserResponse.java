package com.myorg.is.model.dto.response;

/**
 * Request DTO for creating a new app user.
 *
 * @param id        ID of the user.
 * @param firstName First name of the user.
 * @param lastName  Last name of the user.
 * @param email     Email of the user.
 */
public record AppUserResponse(Long id, String firstName, String lastName, String email) {

}

