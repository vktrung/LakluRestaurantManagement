package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.MenuItemPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.MenuItemRequest;
import com.laklu.pos.dataObjects.response.MenuItemResponse;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.MenuItemMapper;
import com.laklu.pos.services.MenuItemService;
import com.laklu.pos.uiltis.Ultis;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu-items")
@AllArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final MenuItemPolicy menuItemPolicy;
    private final MenuItemMapper menuItemMapper;

    //Lấy danh sách tất cả MenuItems
    @GetMapping
    public ApiResponseEntity getAllMenuItems() throws Exception {
        Ultis.throwUnless(menuItemPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        List<MenuItemResponse> menuItemResponses = menuItemMapper.toResponseList(menuItems);
        return ApiResponseEntity.success(menuItemResponses, "Lấy danh sách menu items thành công");
    }

    //Lấy thông tin một MenuItem theo ID
    @GetMapping("/{id}")
    public ApiResponseEntity getMenuItemById(@PathVariable Long id) throws Exception {
        var menuItemExist = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canView(JwtGuard.userPrincipal(),  menuItemExist), new ForbiddenException());
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        MenuItemResponse menuItemResponse = menuItemMapper.toResponse(menuItem);
        return ApiResponseEntity.success(menuItemResponse, "Lấy thông tin menu item thành công");
    }

    //Thêm mới MenuItem
    @PostMapping
    public ApiResponseEntity createMenuItem(@Valid @RequestBody MenuItemRequest request) throws Exception {
        Ultis.throwUnless(menuItemPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        MenuItem menuItem = menuItemService.createMenuItem(request);
        MenuItemResponse menuItemResponse = menuItemMapper.toResponse(menuItem);
        return ApiResponseEntity.success(menuItemResponse, "Tạo menu item thành công");
    }

    //Cập nhật MenuItem
    @PatchMapping("/{id}")
    public ApiResponseEntity updateMenuItemPartially(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws Exception {
        var menuItemExist = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canEdit(JwtGuard.userPrincipal(), menuItemExist), new ForbiddenException());
        MenuItem updatedMenuItem = menuItemService.updateMenuItemPartially(id, updates);
        MenuItemResponse menuItemResponse = menuItemMapper.toResponse(updatedMenuItem);
        return ApiResponseEntity.success(menuItemResponse, "Cập nhật menu item thành công (Partial Update)");
    }


    //Xóa mềm MenuItem
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteMenuItem(@PathVariable Long id) throws Exception {
        var menuItem = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canDelete(JwtGuard.userPrincipal(), menuItem), new ForbiddenException());
        menuItemService.deleteMenuItem(id);
        return ApiResponseEntity.success(null, "Xóa menu item thành công");
    }

}
