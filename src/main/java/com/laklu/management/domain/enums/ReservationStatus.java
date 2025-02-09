package com.laklu.management.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    CANCELED("Đã hủy");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
