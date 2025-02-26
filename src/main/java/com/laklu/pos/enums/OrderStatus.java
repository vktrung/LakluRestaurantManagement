package com.laklu.pos.enums;

public enum OrderStatus {
    PENDING("Đang chờ"),
    CONFIRMED("Đã xác nhận"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy"),;

    OrderStatus(String description) {
        this.description = description;
    }

    private final String description;
}
