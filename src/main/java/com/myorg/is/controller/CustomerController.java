package com.myorg.is.controller;

import static com.myorg.is.util.enums.Status.SUCCESS;

import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.dto.response.MetaData;
import com.myorg.is.model.dto.response.StandardApiResponse;
import com.myorg.is.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private final CustomerService customerService;

  /**
   * Creates a new customer based on the provided request data.
   *
   * @param customerRequest the request body containing the customer details to be created.
   * @return a ResponseEntity containing the created customer details and a success status.
   */
  @PostMapping
  public ResponseEntity<StandardApiResponse> createCustomer(
      @Valid @RequestBody CustomerRequest customerRequest) {
    log.debug("Create customer request received: {}", customerRequest);

    CustomerResponse customerResponse = customerService.createCustomer(customerRequest);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(customerResponse.id()).toUri();

    return ResponseEntity
        .created(location)
        .body(new StandardApiResponse(new MetaData(SUCCESS), customerResponse));
  }

  /**
   * Retrieves a customer by the unique identifier of id.
   *
   * @param id the unique identifier of the customer to be retrieved, provided as a path variable.
   * @return a ResponseEntity containing the customer details and a success status if found, or an
   * appropriate error response if not found or if validation fails.
   */
  @GetMapping("/{id}")
  public ResponseEntity<StandardApiResponse> getCustomerById(
      @Valid @PathVariable @NotNull(message = "{pathVariable.id.isRequired}") Long id) {
    log.debug("Get customer by ID request received: {}", id);

    return ResponseEntity.ok(
        new StandardApiResponse(new MetaData(SUCCESS), customerService.getCustomerById(id)));
  }

  /**
   * Updates an existing customer identified by the provided id with the new details specified in
   * the request body.
   *
   * @param id              the unique identifier of the customer to be updated, provided as a path
   *                        variable.
   * @param customerRequest the request body containing the updated customer details.
   * @return a ResponseEntity containing the updated customer details and a success status if the
   * update is successful, or an appropriate error response if the customer is not found, if
   * validation fails, or if there are business rule violations (e.g., email already associated with
   * another customer).
   */
  @PutMapping("/{id}")
  public ResponseEntity<StandardApiResponse> updateCustomer(
      @Valid @PathVariable @NotNull(message = "{pathVariable.id.isRequired}") Long id,
      @Valid @RequestBody CustomerRequest customerRequest) {
    log.debug("Update customer request received for ID {}: {}", id, customerRequest);

    return ResponseEntity.ok(new StandardApiResponse(new MetaData(SUCCESS),
        customerService.updateCustomer(id, customerRequest)));
  }

  /**
   * Deletes an existing customer identified by the provided id.
   *
   * @param id the unique identifier of the customer to be deleted, provided as a path variable.
   * @return a ResponseEntity with no content if the deletion is successful, or an appropriate error
   * response if the customer is not found or if validation fails.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<StandardApiResponse> deleteCustomer(
      @Valid
      @PathVariable
      @NotNull(message = "{pathVariable.id.isRequired}") Long id) {
    log.debug("Delete customer request received for ID: {}", id);

    customerService.deleteCustomerById(id);
    return ResponseEntity.noContent().build();
  }
}
