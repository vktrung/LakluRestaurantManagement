package com.laklu.pos.auth;

import com.laklu.pos.enums.PermissionGroup;
import lombok.Getter;


@Getter
public enum PermissionAlias {

    // User Permissions
    CREATE_USER("users:create", "Thêm nhân viên", PermissionGroup.USER),
    UPDATE_USER("users:update", "Sửa nhân viên", PermissionGroup.USER),
    LIST_USER("users:list", "Danh sách nhân viên", PermissionGroup.USER),
    DELETE_USER("users:delete", "Xóa nhân viên", PermissionGroup.USER),

    // Role Permissions
    CREATE_ROLE("roles:create", "Thêm nhóm quyền", PermissionGroup.ROLE),
    UPDATE_ROLE("roles:update", "Sửa nhóm quyền", PermissionGroup.ROLE),
    LIST_ROLE("roles:list", "Danh sách nhóm quyền", PermissionGroup.ROLE),
    DELETE_ROLE("roles:delete", "Xóa nhóm quyền", PermissionGroup.ROLE),

    // Table Permissions
    CREATE_TABLE("tables:create", "Thêm bàn"),
    UPDATE_TABLE("tables:update", "Sửa bàn"),
    LIST_TABLE("tables:list", "Danh sách bàn"),
    DELETE_TABLE("tables:delete", "Xóa bàn"),

    // Reservation Permissions
    CREATE_RESERVATION("reservations:create", "Tạo đặt chỗ"),
    UPDATE_RESERVATION("reservations:update", "Cập nhật đặt chỗ"),
    LIST_RESERVATION("reservations:list", "Danh sách đặt chỗ"),
    DELETE_RESERVATION("reservations:delete", "Xóa đặt chỗ"),
    VIEW_RESERVATION("reservations:view", "Xem chi tiết đặt chỗ");

    CREATE_ATTACHMENT("attachments:create", "Tải lên tệp đính kèm", PermissionGroup.ATTACHMENT),
    UPDATE_ATTACHMENT("attachments:update", "Cập nhật tệp đính kèm", PermissionGroup.ATTACHMENT),
    LIST_ATTACHMENT("attachments:list", "Danh sách tệp đính kèm", PermissionGroup.ATTACHMENT),
    DELETE_ATTACHMENT("attachments:delete", "Xóa tệp đính kèm", PermissionGroup.ATTACHMENT),
    VIEW_ATTACHMENT("attachments:view", "Xem chi tiết tệp đính kèm", PermissionGroup.ATTACHMENT),


    CREATE_CATEGORY("categories:create","Thêm danh mục", PermissionGroup.CATEGORY),
    UPDATE_CATEGORY("categories:update","Sửa danh mục", PermissionGroup.CATEGORY),
    LIST_CATEGORY("categories:list","Danh sách danh mục", PermissionGroup.CATEGORY),
    DELETE_CATEGORY("categories:delete","Xóa danh mục", PermissionGroup.CATEGORY);

    PermissionAlias(String alias, String name, PermissionGroup group) {
        this.alias = alias;
        this.name = name;
        this.group = group;
    }

    private final String alias;
    private final String name;
    private final PermissionGroup group;
}
