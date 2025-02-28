package com.laklu.pos.enums;

import lombok.Getter;

@Getter
public enum PermissionGroup {
    USER("Quyền Người Dùng", "USER", "Quyền liên quan đến người dùng"),
    ROLE("Quyền Vai Trò", "ROLE", "Quyền liên quan đến vai trò"),
    ATTACHMENT("Quyền Tệp Đính Kèm", "ATTACHMENT", "Quyền liên quan đến tệp đính kèm"),
    SCHEDULE("Quyền Lịch Làm Việc", "SCHEDULE", "Quyền liên quan đến lịch làm việc"),
    CATEGORY("Quyền Danh Mục", "CATEGORY", "Quyền liên quan đến danh mục"),
    TABLE("Quyền Bàn", "TABLE", "Quyền liên quan đến bàn"),
    RESERVATION("Quyền Đặt Chỗ", "RESERVATION", "Quyền liên quan đến đặt chỗ"),
    MENU("Quyền Menu", "MENU", "Quyền liên quan đến menu"),
    DISH("Quyền Món Ăn", "DISH", "Quyền liên quan đến món ăn"),
    MENU_ITEM("Quyền Mục Menu", "MENU_ITEM", "Quyền liên quan đến mục menu"),
    SALARY_RATE("Quyền Mức Lương", "SALARY_RATE", "Quyền liên quan đến mức lương");

    private final String label;
    private final String alias;
    private final String description;

    PermissionGroup(String label, String alias, String description) {
        this.label = label;
        this.alias = alias;
        this.description = description;
    }
}