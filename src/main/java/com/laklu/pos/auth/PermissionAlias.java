package com.laklu.pos.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum PermissionAlias {

    CREATE_USER("users:create", "Thêm nhân viên"),
    UPDATE_USER("users:update", "Sửa nhân viên"),
    LIST_USER("users:list", "Danh sách nhân viên"),
    DELETE_USER("users:delete", "Xóa nhân viên");


    PermissionAlias(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }

    private final String alias;
    private final String name;
}
