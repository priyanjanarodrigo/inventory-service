package com.myorg.is.model.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
