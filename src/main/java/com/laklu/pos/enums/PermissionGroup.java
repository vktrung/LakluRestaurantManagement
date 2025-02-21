package com.laklu.pos.enums;

import lombok.Getter;

@Getter
public enum PermissionGroup {
    USER("User Permissions", "USER", "Quyền liên quan đến người dùng"),
    ROLE("Role Permissions", "ROLE", "Quyền liên quan đến vai trò"),
    ATTACHMENT("Attachment Permissions", "ATTACHMENT", "Quyền liên quan đến tệp đính kèm"),
    SCHEDULE("Schedule Permissions", "SCHEDULE", "Quyền liên quan đến lịch làm việc"),
    CATEGORY("Category Permissions", "CATEGORY", "Quyền liên quan đến danh mục"),
    TABLE("Table Permissions", "TABLE", "Quyền liên quan đến bàn"),
    RESERVATION("Reservation Permissions", "RESERVATION", "Quyền liên quan đến đặt chỗ"),
    SALARY_RATE("Salary Rate Permissions", "SALARY_RATE", "Quyền liên quan đến mức lương"),
    PAYMENT("Payment Permissions", "PAYMENT", "Quyền liên quan đến thanh toán");

    private final String label;
    private final String alias;
    private final String description;

    PermissionGroup(String label, String alias, String description) {
        this.label = label;
        this.alias = alias;
        this.description = description;
    }
}
