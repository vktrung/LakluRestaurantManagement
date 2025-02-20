package com.laklu.pos.enums;

import lombok.Getter;

@Getter
public enum PermissionGroup {
    USER("User Permissions", "USER", "Quyền liên quan đến người dùng"),
    ROLE("Role Permissions", "ROLE", "Quyền liên quan đến vai trò"),
    ATTACHMENT("Attachment Permissions", "ATTACHMENT", "Quyền liên quan đến tệp đính kèm"),
    CATEGORY("Category Permissions", "CATEGORY", "Quyền liên quan đến danh mục"),
    MENU_ITEM("Menu Item Permissions", "MENU_ITEM", "Quyền liên quan đến món ăn");

    private final String label;
    private final String alias;
    private final String description;

    PermissionGroup(String label, String alias, String description) {
        this.label = label;
        this.alias = alias;
        this.description = description;
    }
}
