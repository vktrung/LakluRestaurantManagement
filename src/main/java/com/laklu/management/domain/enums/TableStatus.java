package com.laklu.management.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TableStatus {

    AVAILABLE("Còn trống"),
    OCCUPIED("Đã có khách"),
    RESERVED("Đã đặt trước");

    private final String description;

    TableStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
