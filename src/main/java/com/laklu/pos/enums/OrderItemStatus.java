package com.laklu.pos.enums;

public enum OrderItemStatus {
    PENDING("Đang chờ"),
    CONFIRMED("Đã xác nhận"),
    DOING("Đang làm"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy"),;

    OrderItemStatus(String description) {
        this.description = description;
    }

    private final String description;
}
