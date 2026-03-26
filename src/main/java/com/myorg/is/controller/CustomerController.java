package com.myorg.is.controller;

import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.dto.response.MetaData;
import com.myorg.is.model.dto.response.StandardApiResponse;
import com.myorg.is.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.myorg.is.util.enums.Status.SUCCESS;

/**
 * CustomerController is a REST controller that handles HTTP requests related to customer resources.
 * It provides endpoints for creating, retrieving, updating, and deleting customers.
 *
 * <p>This controller delegates business logic to the {@link CustomerService} and focuses on
 * request handling, validation, and response construction.</p>
 */
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

    /**
     * Retrieves a paginated list of all customers.
     *
     * @param pageable the pagination information, including page number, page size, and sorting options, provided
     *                 as query parameters.
     * @return a ResponseEntity containing a paginated list of customers and a success status, or an appropriate error
     * response if validation fails.
     */
    @GetMapping
    public ResponseEntity<StandardApiResponse> getAllCustomers(Pageable pageable) {
        log.debug("Get all customers request received");

        return ResponseEntity.ok(new StandardApiResponse(new MetaData(SUCCESS),
                customerService.getAllCustomers(pageable)));
    }
}
