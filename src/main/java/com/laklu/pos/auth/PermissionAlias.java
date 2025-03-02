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

    CREATE_TABLE("tables:create", "Thêm bàn", PermissionGroup.TABLE),
    UPDATE_TABLE("tables:update", "Sửa bàn", PermissionGroup.TABLE),
    LIST_TABLE("tables:list", "Danh sách bàn", PermissionGroup.TABLE),
    DELETE_TABLE("tables:delete", "Xóa bàn", PermissionGroup.TABLE),

    CREATE_RESERVATION("reservations:create", "Tạo đặt chỗ", PermissionGroup.RESERVATION),
    UPDATE_RESERVATION("reservations:update", "Cập nhật đặt chỗ", PermissionGroup.RESERVATION),
    LIST_RESERVATION("reservations:list", "Danh sách đặt chỗ", PermissionGroup.RESERVATION),
    DELETE_RESERVATION("reservations:delete", "Xóa đặt chỗ", PermissionGroup.RESERVATION),
    VIEW_RESERVATION("reservations:view", "Xem chi tiết đặt chỗ", PermissionGroup.RESERVATION),

    CREATE_ATTACHMENT("attachments:create", "Tải lên tệp đính kèm", PermissionGroup.ATTACHMENT),
    UPDATE_ATTACHMENT("attachments:update", "Cập nhật tệp đính kèm", PermissionGroup.ATTACHMENT),
    LIST_ATTACHMENT("attachments:list", "Danh sách tệp đính kèm", PermissionGroup.ATTACHMENT),
    DELETE_ATTACHMENT("attachments:delete", "Xóa tệp đính kèm", PermissionGroup.ATTACHMENT),
    VIEW_ATTACHMENT("attachments:view", "Xem chi tiết tệp đính kèm", PermissionGroup.ATTACHMENT),

    CREATE_SCHEDULE("schedules:create", "Tạo lịch làm việc", PermissionGroup.SCHEDULE),
    UPDATE_SCHEDULE("schedules:update", "Cập nhật lịch làm việc", PermissionGroup.SCHEDULE),
    LIST_SCHEDULE("schedules:list", "Xem danh sách lịch làm việc", PermissionGroup.SCHEDULE),
    DELETE_SCHEDULE("schedules:delete", "Xóa lịch làm việc", PermissionGroup.SCHEDULE),
    CREATE_SCHEDULE_CHECK_IN_QR_CODE("schedules:create_check_in_qr_code", "Tạo mã QR điểm danh", PermissionGroup.SCHEDULE),
    CREATE_SCHEDULE_CHECK_OUT_QR_CODE("schedules:create_check_out_qr_code", "Tạo mã QR Checkout", PermissionGroup.SCHEDULE),

    CREATE_SALARY_RATE("salary_rate:create", "Thêm mức lương", PermissionGroup.SALARY_RATE),
    UPDATE_SALARY_RATE("salary_rate:update", "Sửa mức lương", PermissionGroup.SALARY_RATE),
    LIST_SALARY_RATE("salary_rate:list", "Danh sách mức lương", PermissionGroup.SALARY_RATE),
    DELETE_SALARY_RATE("salary_rate:delete", "Xóa mức lương", PermissionGroup.SALARY_RATE),

    CREATE_MENU("menus:create", "Thêm menu", PermissionGroup.MENU),
    UPDATE_MENU("menus:update", "Sửa menu", PermissionGroup.MENU),
    LIST_MENU("menus:list", "Danh sách menu", PermissionGroup.MENU),
    DELETE_MENU("menus:delete", "Xóa menu", PermissionGroup.MENU),

    CREATE_MENU_ITEM("menu_items:create", "Thêm mục menu", PermissionGroup.MENU_ITEM),
    UPDATE_MENU_ITEM("menu_items:update", "Sửa mục menu", PermissionGroup.MENU_ITEM),
    LIST_MENU_ITEM("menu_items:list", "Danh sách mục menu", PermissionGroup.MENU_ITEM),
    DELETE_MENU_ITEM("menu_items:delete", "Xóa mục menu", PermissionGroup.MENU_ITEM),

    CREATE_DISH("dishes:create", "Thêm món ăn", PermissionGroup.DISH),
    UPDATE_DISH("dishes:update", "Sửa món ăn", PermissionGroup.DISH),
    LIST_DISH("dishes:list", "Danh sách món ăn", PermissionGroup.DISH),
    DELETE_DISH("dishes:delete", "Xóa món ăn", PermissionGroup.DISH),

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
