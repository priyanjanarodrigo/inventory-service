package com.myorg.is.util.statics;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Constants class containing static final values used throughout the application.
 */
@NoArgsConstructor(access = PRIVATE)
public final class Constants {

  public static final int INT_ONE = 1;
  public static final int INT_ZERO = 0;
  public static final String ERROR_CUSTOMER_ID_CANNOT_BE_NULL = "Customer Id cannot be null";
  public static final String ERROR_CUSTOMER_ID_MUST_BE_GREATER_THAN_ZERO = "Customer Id must be greater than zero";
  public static final String ERROR_CUSTOMER_NOT_FOUND_FOR_ID = "Customer not found for Id: %s";
  public static final String ERROR_EMAIL_ALREADY_TAKEN = "Email %s already taken, please provide a different email";
  public static final String ERROR_EMAIL_ALREADY_ASSOCIATED_WITH_CUSTOMER = "Email is already associated with this particular customer, please provide a different email";
  public static final String ERROR_EMAIL_IS_REQUIRED = "Email is required and cannot be null or empty";
}
