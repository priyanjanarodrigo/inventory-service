package com.myorg.is.model.dto.response;

/**
 * CustomerResponse is a DTO representing the response for customer-related API endpoints.
 *
 * @param id        Unique identifier of the customer
 * @param firstName First name of the customer
 * @param lastName  Last name of the customer
 * @param email     Email address of the customer
 */
public record CustomerResponse(Long id, String firstName, String lastName, String email) {

}
