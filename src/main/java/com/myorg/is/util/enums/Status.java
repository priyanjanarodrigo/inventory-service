package com.myorg.is.util.enums;

import lombok.Getter;

/**
 * Status is an enum representing the status of an API response.
 * It has two possible values: SUCCESS and ERROR.
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
