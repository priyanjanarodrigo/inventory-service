package com.myorg.is.service;

import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.entity.Customer;
import com.myorg.is.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service("customerService")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final ObjectMapper objectMapper;

  private final CustomerRepository customerRepository;

  @Override
  public CustomerResponse createCustomer(CustomerRequest customerRequest) {
    Customer customer = customerRepository.save(
        objectMapper.convertValue(customerRequest, Customer.class));
    return objectMapper.convertValue(customer, CustomerResponse.class);
  }
}
