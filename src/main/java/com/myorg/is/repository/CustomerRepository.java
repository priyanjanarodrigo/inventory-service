package com.myorg.is.repository;

import com.myorg.is.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CustomerRepository is a Spring Data JPA repository interface for managing Customer entities. It extends
 * JpaRepository, providing CRUD operations and additional query methods for interacting with the
 * underlying database. This repository includes a custom method to retrieve a customer by their email
 * address.
 */
@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieves a customer by their email address. This method executes a custom JPQL query to find a
     * customer entity where the email field matches the provided email parameter. The result is
     * returned as an Optional, which will contain the customer if found, or be empty if no customer
     * with the specified email exists in the database.
     *
     * @param email the email address of the customer to be retrieved, which must not be null and
     *              should be a valid email format.
     * @return an Optional containing the customer entity if a customer with the specified email is
     * found; otherwise, an empty Optional is returned, indicating that no customer exists for the
     * given email address.
     */
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> getCustomerByEmail(@Param("email") String email);
}
