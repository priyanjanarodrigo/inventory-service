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

  @GetMapping("/{id}")
  public ResponseEntity<StandardApiResponse> getCustomerById(
      @Valid @PathVariable @NotNull(message = "{pathVariable.id.isRequired}") Long id) {
    log.debug("Get customer by ID request received: {}", id);

    return ResponseEntity.ok(
        new StandardApiResponse(new MetaData(SUCCESS), customerService.getCustomerById(id)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<StandardApiResponse> updateCustomer(
      @Valid @PathVariable @NotNull(message = "{pathVariable.id.isRequired}") Long id,
      @Valid @RequestBody CustomerRequest customerRequest) {
    log.debug("Update customer request received for ID {}: {}", id, customerRequest);

    return ResponseEntity.ok(new StandardApiResponse(new MetaData(SUCCESS),
        customerService.updateCustomer(id, customerRequest)));
  }

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
