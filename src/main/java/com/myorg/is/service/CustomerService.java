package com.myorg.is.service;

import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;

/**
 * Service interface defining the business operations for managing
 * {@link com.myorg.is.model.entity.Customer} resources.
 *
 * <p>This interface abstracts the core customer-related business logic and acts as a contract
 * between the controller layer and the underlying service implementation.</p>
 *
 * <p>Implementations of this interface are responsible for handling business rules,
 * validation orchestration, and persistence coordination for customer operations.</p>
 */
public interface CustomerService {

  CustomerResponse createCustomer(CustomerRequest customerRequest);

  CustomerResponse getCustomerById(Long id);

  CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest);

  void deleteCustomerById(Long id);
}
