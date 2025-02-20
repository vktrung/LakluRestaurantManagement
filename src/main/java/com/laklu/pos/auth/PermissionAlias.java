package com.laklu.pos.auth;

import com.laklu.pos.enums.PermissionGroup;
import lombok.Getter;


@Getter
public enum PermissionAlias {

    CREATE_USER("users:create", "Thêm nhân viên", PermissionGroup.USER),
    UPDATE_USER("users:update", "Sửa nhân viên", PermissionGroup.USER),
    LIST_USER("users:list", "Danh sách nhân viên", PermissionGroup.USER),
    DELETE_USER("users:delete", "Xóa nhân viên", PermissionGroup.USER),

    CREATE_ROLE("roles:create", "Thêm nhóm quyền", PermissionGroup.ROLE),
    UPDATE_ROLE("roles:update", "Sửa nhóm quyền", PermissionGroup.ROLE),
    LIST_ROLE("roles:list", "Danh sách nhóm quyền", PermissionGroup.ROLE),
    DELETE_ROLE("roles:delete", "Xóa nhóm quyền", PermissionGroup.ROLE),

    CREATE_ATTACHMENT("attachments:create", "Tải lên tệp đính kèm", PermissionGroup.ATTACHMENT),
    UPDATE_ATTACHMENT("attachments:update", "Cập nhật tệp đính kèm", PermissionGroup.ATTACHMENT),
    LIST_ATTACHMENT("attachments:list", "Danh sách tệp đính kèm", PermissionGroup.ATTACHMENT),
    DELETE_ATTACHMENT("attachments:delete", "Xóa tệp đính kèm", PermissionGroup.ATTACHMENT),
    VIEW_ATTACHMENT("attachments:view", "Xem chi tiết tệp đính kèm", PermissionGroup.ATTACHMENT),

    CREATE_CATEGORY("categories:create","Thêm danh mục", PermissionGroup.CATEGORY),
    UPDATE_CATEGORY("categories:update","Sửa danh mục", PermissionGroup.CATEGORY),
    LIST_CATEGORY("categories:list","Danh sách danh mục", PermissionGroup.CATEGORY),
    DELETE_CATEGORY("categories:delete","Xóa danh mục", PermissionGroup.CATEGORY),

    CREATE_MENU_ITEM("menuItems:create","Thêm món ăn", PermissionGroup.MENU_ITEM),
    UPDATE_MENU_ITEM("menuItems:update","Sửa món ăn", PermissionGroup.MENU_ITEM),
    LIST_MENU_ITEM("menuItems:list","Danh sách món ăn", PermissionGroup.MENU_ITEM),
    VIEW_MENU_ITEM("menuItems:view","Xem chi tiết món ăn", PermissionGroup.MENU_ITEM),
    DELETE_MENU_ITEM("menuItems:delete","Xóa món ăn", PermissionGroup.MENU_ITEM);

    PermissionAlias(String alias, String name, PermissionGroup group) {
        this.alias = alias;
        this.name = name;
        this.group = group;
    }

    private final String alias;
    private final String name;
    private final PermissionGroup group;
}
