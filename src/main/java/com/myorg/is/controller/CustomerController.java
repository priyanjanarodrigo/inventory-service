package com.myorg.is.controller;

import static com.myorg.is.util.enums.Status.SUCCESS;

import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.dto.response.MetaData;
import com.myorg.is.model.dto.response.StandardApiResponse;
import com.myorg.is.service.CustomerService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
}
