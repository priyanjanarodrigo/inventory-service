package com.myorg.is.service;

import static com.myorg.is.util.statics.Constants.ERROR_CUSTOMER_ID_CANNOT_BE_NULL;
import static com.myorg.is.util.statics.Constants.ERROR_CUSTOMER_ID_MUST_BE_GREATER_THAN_ZERO;
import static com.myorg.is.util.statics.Constants.ERROR_CUSTOMER_NOT_FOUND_FOR_ID;
import static com.myorg.is.util.statics.Constants.ERROR_EMAIL_ALREADY_ASSOCIATED_WITH_CUSTOMER;
import static com.myorg.is.util.statics.Constants.ERROR_EMAIL_ALREADY_TAKEN;
import static com.myorg.is.util.statics.Constants.ERROR_EMAIL_IS_REQUIRED;
import static com.myorg.is.util.statics.Constants.INT_ONE;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.myorg.is.exception.StandardApiException;
import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.entity.Customer;
import com.myorg.is.repository.CustomerRepository;
import java.util.Objects;
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
    checkIfEmailAlreadyTaken(customerRequest.email());

    Customer customer = customerRepository.save(
        objectMapper.convertValue(customerRequest, Customer.class));
    return objectMapper.convertValue(customer, CustomerResponse.class);
  }

  @Override
  public CustomerResponse getCustomerById(Long id) {
    return objectMapper.convertValue(getExistingCustomerById(id), CustomerResponse.class);
  }

  @Override
  public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
    Customer customer = getExistingCustomerById(id);

    if (Objects.equals(customer.getEmail(), customerRequest.email())) {
      throw new StandardApiException(CONFLICT, ERROR_EMAIL_ALREADY_ASSOCIATED_WITH_CUSTOMER);
    }

    checkIfEmailAlreadyTaken(customerRequest.email());

    customer.setFirstName(customerRequest.firstName());
    customer.setLastName(customerRequest.lastName());
    customer.setEmail(customerRequest.email());

    return objectMapper.convertValue(customerRepository.save(customer), CustomerResponse.class);
  }

  @Override
  public void deleteCustomerById(Long id) {
    Customer customer = getExistingCustomerById(id);
    customerRepository.delete(customer);
  }


  private Customer getExistingCustomerById(Long id) {
    if (isNull(id)) {
      throw new StandardApiException(BAD_REQUEST, ERROR_CUSTOMER_ID_CANNOT_BE_NULL);
    }

    if (id < INT_ONE) {
      throw new StandardApiException(BAD_REQUEST, ERROR_CUSTOMER_ID_MUST_BE_GREATER_THAN_ZERO);
    }

    return customerRepository.findById(id).orElseThrow(() -> new StandardApiException(
        NOT_FOUND, format(ERROR_CUSTOMER_NOT_FOUND_FOR_ID, id)));
  }

  private boolean isCustomerExistByEmail(String email) {
    if (isNull(email) || email.isBlank()) {
      throw new StandardApiException(BAD_REQUEST, ERROR_EMAIL_IS_REQUIRED);
    }

    return customerRepository.getCustomerByEmail(email).isPresent();
  }

  /**
   * Checks if the provided email is already associated with an existing customer. If the email is
   * already taken, a {@link StandardApiException} with a CONFLICT status is thrown, indicating that
   * the email is not available for use. This method is used to enforce the uniqueness constraint on
   * customer email addresses and prevent duplicate entries in the system.
   *
   * @param email Email address.
   */
  private void checkIfEmailAlreadyTaken(String email) {
    if (isCustomerExistByEmail(email)) {
      throw new StandardApiException(CONFLICT, format(ERROR_EMAIL_ALREADY_TAKEN, email));
    }
  }
}
