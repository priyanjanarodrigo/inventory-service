package com.myorg.is.util.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of an operation.
 */
@Getter
public enum Status {
  SUCCESS("success"),
  ERROR("error");

  private final String name;

  Status(String name) {
    this.name = name;
  }
}
