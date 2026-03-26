package com.myorg.is.model.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * CustomerRequest is a record class that represents the request body for creating or updating a customer.
 * It includes validation annotations to ensure that the required fields are provided and properly formatted.
 *
 * @param firstName the first name of the customer, which is required and cannot be blank.
 * @param lastName  the last name of the customer, which is required and cannot be blank.
 * @param email     the email address of the customer, which is required, cannot be blank, and must be
 *                  in a valid email format.
 */
public record CustomerRequest(
        @NotNull(message = "{request.firstName.isRequired}")
        @NotBlank(message = "{request.firstName.isRequired}")
        String firstName,
        @NotNull(message = "{request.lastName.isRequired}")
        @NotBlank(message = "{request.lastName.isRequired}")
        String lastName,
        @NotNull(message = "{request.email.isRequired}")
        @NotBlank(message = "{request.email.isRequired}")
        @Email(message = "{request.email.invalidFormat}")
        String email) {

}
