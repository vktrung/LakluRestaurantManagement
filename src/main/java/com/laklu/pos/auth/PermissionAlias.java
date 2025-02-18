package com.laklu.pos.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public enum PermissionAlias {

    CREATE_USER("users:create", "Thêm nhân viên"),
    UPDATE_USER("users:update", "Sửa nhân viên"),
    LIST_USER("users:list", "Danh sách nhân viên"),
    DELETE_USER("users:delete", "Xóa nhân viên"),

    CREATE_ROLE("roles:create", "Thêm nhóm quyền"),
    UPDATE_ROLE("roles:update", "Sửa nhóm quyền"),
    LIST_ROLE("roles:list", "Danh sách nhóm quyền"),
    DELETE_ROLE("roles:delete", "Xóa nhóm quyền"),

    CREATE_ATTACHMENT("attachments:create", "Tải lên tệp đính kèm"),
    UPDATE_ATTACHMENT("attachments:update", "Cập nhật tệp đính kèm"),
    LIST_ATTACHMENT("attachments:list", "Danh sách tệp đính kèm"),
    DELETE_ATTACHMENT("attachments:delete", "Xóa tệp đính kèm"),
    VIEW_ATTACHMENT("attachments:view", "Xem chi tiết tệp đính kèm"),

    CREATE_CATEGORY("categories:create","Thêm danh mục"),
    UPDATE_CATEGORY("categories:update","Sửa danh mục"),
    LIST_CATEGORY("categories:list","Danh sách danh mục"),
    DELETE_CATEGORY("categories:delete","Xóa danh mục");

    PermissionAlias(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }

    private final String alias;
    private final String name;
}
