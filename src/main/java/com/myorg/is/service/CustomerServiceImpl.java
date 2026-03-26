package com.myorg.is.service;

import com.myorg.is.exception.StandardApiException;
import com.myorg.is.model.dto.request.CustomerRequest;
import com.myorg.is.model.dto.response.CustomerResponse;
import com.myorg.is.model.entity.Customer;
import com.myorg.is.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

import static com.myorg.is.util.statics.Constants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.*;

/**
 * CustomerServiceImpl is the implementation of the {@link CustomerService} interface, providing
 * business logic for managing {@link Customer} resources. This service handles operations such as
 * creating, retrieving, updating, and deleting customers while ensuring that business rules and
 * validation are properly enforced.
 *
 * <p>The service interacts with the {@link CustomerRepository} to perform database operations and
 * uses an {@link ObjectMapper} for converting between entity and DTO objects.</p>
 */
@Service("customerService")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ObjectMapper objectMapper;

    private final CustomerRepository customerRepository;

    /**
     * Creates a new customer based on the provided request data. This method first checks if the
     * email in the request is already taken by another customer; if so, a
     * {@link StandardApiException} with a CONFLICT status is thrown, indicating that the email is not
     * available for use and prompting the user to provide a different email address. If the email is
     * valid and not taken, a new customer entity is created from the request data, saved to the
     * repository, and the created customer details are returned as a {@link CustomerResponse}.
     *
     * @param customerRequest the request body containing the customer details to be created, which
     *                        must not be null and must contain valid data according to the defined
     *                        constraints.
     * @return the created customer details as a {@link CustomerResponse} if the creation is
     * successful; otherwise, an appropriate exception is thrown indicating the reason for the failure
     * (e.g., email already taken, etc.).
     */
    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        checkIfEmailAlreadyTaken(customerRequest.email());

        Customer customer = customerRepository.save(
                objectMapper.convertValue(customerRequest, Customer.class));
        return objectMapper.convertValue(customer, CustomerResponse.class);
    }

    /**
     * Retrieves a customer by their unique identifier (ID). This method first validates the provided
     * ID to ensure it is not null and is greater than zero. If the ID is valid, it attempts to find
     * the customer in the repository. If a customer with the specified ID is found, it is returned as
     * a {@link CustomerResponse}; otherwise, a {@link StandardApiException} with a NOT_FOUND status
     * is thrown, indicating that no customer exists for the given ID.
     *
     * @param id the unique identifier of the customer to be retrieved, which must not be null and
     *           must be greater than zero.
     * @return the customer details as a {@link CustomerResponse} if a customer with the specified ID
     * is found; otherwise, an exception is thrown indicating that the customer was not found.
     */
    @Override
    public CustomerResponse getCustomerById(Long id) {
        return objectMapper.convertValue(getExistingCustomerById(id), CustomerResponse.class);
    }

    /**
     * Updates an existing customer identified by their unique identifier (ID) with the provided
     * request data. This method first retrieves the existing customer using the provided ID to ensure
     * that the customer exists. If the customer is found, it checks if the email in the request is
     * already associated with the same customer; if so, a {@link StandardApiException} with a
     * CONFLICT status is thrown, indicating that the email is already associated with this particular
     * customer. If the email is different, it checks if the new email is already taken by another
     * customer; if so, a {@link StandardApiException} with a CONFLICT status is thrown, indicating
     * that the email is already taken and prompting the user to provide a different email address. If
     * the email is valid and not taken, the customer's details are updated with the new information
     * from the request, and the updated customer is saved to the repository. Finally, the updated
     * customer details are returned as a {@link CustomerResponse}.
     *
     * @param id              the unique identifier of the customer to be updated, which must not be
     *                        null and must be greater than zero.
     * @param customerRequest the request body containing the updated customer details, which must not
     *                        be null and must contain valid data according to the defined
     *                        constraints.
     * @return the updated customer details as a {@link CustomerResponse} if the update is successful;
     * otherwise, an appropriate exception is thrown indicating the reason for the failure (e.g.,
     * customer not found, email already associated with the same customer, email already taken by
     * another customer, etc.).
     */
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

    /**
     * Deletes an existing customer identified by their unique identifier (ID). This method first
     * retrieves the customer using the provided ID to ensure that the customer exists. If the
     * customer is found, it is deleted from the repository. If the customer does not exist, a
     * {@link StandardApiException} with a NOT_FOUND status is thrown, indicating that no customer
     * exists for the given ID.
     *
     * @param id the unique identifier of the customer to be deleted, which must not be null and must
     *           be greater than zero.
     */
    @Override
    public void deleteCustomerById(Long id) {
        Customer customer = getExistingCustomerById(id);
        customerRepository.delete(customer);
    }

    /**
     * Retrieves an existing customer by their unique identifier (ID). This method performs validation
     * on the provided ID to ensure it is not null and is greater than zero. If the ID is valid, it
     * attempts to find the customer in the repository. If a customer with the specified ID is found,
     * it is returned; otherwise, a {@link StandardApiException} with a NOT_FOUND status is thrown,
     * indicating that no customer exists for the given ID.
     *
     * @param id the unique identifier of the customer to be retrieved, which must not be null and
     *           must be greater than zero.
     * @return the existing customer associated with the provided ID if found; otherwise, an exception
     * is thrown indicating that the customer was not found.
     */
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

    /**
     * Checks if a customer exists with the provided email address. This method first validates the
     * email to ensure it is not null and is not blank. If the email is valid, it queries the
     * repository to check if a customer with the specified email exists. The method returns true if a
     * customer with the given email is found; otherwise, it returns false.
     *
     * @param email the email address to be checked for existence, which must not be null and must not
     *              be blank.
     * @return true if a customer with the specified email exists; otherwise, false.
     */
    private boolean isCustomerExistByEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            throw new StandardApiException(BAD_REQUEST, ERROR_EMAIL_IS_REQUIRED);
        }

        return customerRepository.getCustomerByEmail(email).isPresent();
    }

    /**
     * Checks if the provided email is already associated with an existing customer. If the email is
     * already taken, a {@link StandardApiException} with a CONFLICT status is thrown, indicating that
     * the email is not available for use and prompting the user to provide a different email
     * address.
     *
     * @param email the email address to be checked for uniqueness against existing customers.
     */
    private void checkIfEmailAlreadyTaken(String email) {
        if (isCustomerExistByEmail(email)) {
            throw new StandardApiException(CONFLICT, format(ERROR_EMAIL_ALREADY_TAKEN, email));
        }
    }

    /**
     * Retrieves a paginated list of all customers. This method queries the repository to retrieve all
     * customers in a paginated format based on the provided {@link Pageable} parameter. The retrieved
     * customer entities are then converted to {@link CustomerResponse} DTOs before being returned as a
     * paginated response.
     *
     * @param pageable the pagination information, including page number, page size, and sorting
     *                 criteria, which must not be null.
     * @return a paginated list of customers as {@link CustomerResponse} DTOs.
     */
    @Override
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customer -> objectMapper.convertValue(customer, CustomerResponse.class));
    }
}
