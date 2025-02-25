package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.Policy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewMenuItem;
import com.laklu.pos.dataObjects.response.MenuItemResponse;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.MenuItemMapper;
import com.laklu.pos.services.MenuItemService;
import com.laklu.pos.uiltis.Ultis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "MenuItem Controller", description = "Quản lý thông tin mục trong thực đơn")
@AllArgsConstructor
public class MenuItemController {

    private final Policy<MenuItem> menuItemPolicy; // Giả định có Policy cho MenuItem
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;

    @Operation(summary = "Lấy thông tin tất cả mục trong thực đơn", description = "API này dùng để lấy danh sách tất cả mục trong thực đơn")
    @GetMapping("/")
    public ApiResponseEntity getAllMenuItems() throws Exception {
        Ultis.throwUnless(menuItemPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<MenuItemResponse> menuItems = menuItemService.getAll().stream()
                .map(MenuItemResponse::fromEntity)
                .collect(Collectors.toList());

        return ApiResponseEntity.success(menuItems);
    }

    @Operation(summary = "Tạo một mục trong thực đơn mới", description = "API này dùng để tạo một mục trong thực đơn mới")
    @PostMapping("/")
    public ApiResponseEntity createMenuItem(@RequestBody NewMenuItem newMenuItem) throws Exception {
        Ultis.throwUnless(menuItemPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        MenuItem menuItem = menuItemMapper.toEntity(newMenuItem);
        MenuItem createdMenuItem = menuItemService.createMenuItem(menuItem);
        return ApiResponseEntity.success(MenuItemResponse.fromEntity(createdMenuItem));
    }

    @Operation(summary = "Lấy thông tin mục trong thực đơn theo ID", description = "API này dùng để lấy thông tin mục trong thực đơn theo ID")
    @GetMapping("/{id}")
    public ApiResponseEntity getMenuItemById(@PathVariable Integer id) throws Exception {
        MenuItem menuItem = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canView(JwtGuard.userPrincipal(), menuItem), new ForbiddenException());

        return ApiResponseEntity.success(MenuItemResponse.fromEntity(menuItem));
    }

    @Operation(summary = "Cập nhật thông tin mục trong thực đơn", description = "API này dùng để cập nhật thông tin mục trong thực đơn theo ID")
    @PutMapping("/{id}")
    public ApiResponseEntity updateMenuItem(@PathVariable Integer id, @RequestBody NewMenuItem menuItemDetails) throws Exception {
        MenuItem existingMenuItem = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canEdit(JwtGuard.userPrincipal(), existingMenuItem), new ForbiddenException());

        menuItemMapper.updateMenuItemFromDto(menuItemDetails, existingMenuItem);
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(existingMenuItem);
        return ApiResponseEntity.success(MenuItemResponse.fromEntity(updatedMenuItem));
    }

    @Operation(summary = "Cập nhật một phần thông tin mục trong thực đơn", description = "API này dùng để cập nhật một phần thông tin mục trong thực đơn theo ID")
    @PatchMapping("/{id}")
    public ApiResponseEntity partialUpdateMenuItem(@PathVariable Integer id, @RequestBody NewMenuItem partialUpdateMenuItem) throws Exception {
        MenuItem existingMenuItem = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canEdit(JwtGuard.userPrincipal(), existingMenuItem), new ForbiddenException());

        menuItemMapper.updateMenuItemFromDto(partialUpdateMenuItem, existingMenuItem);
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(existingMenuItem);
        return ApiResponseEntity.success(MenuItemResponse.fromEntity(updatedMenuItem));
    }

    @Operation(summary = "Xóa mục trong thực đơn theo ID", description = "API này dùng để xóa mục trong thực đơn")
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteMenuItem(@PathVariable Integer id) throws Exception {
        MenuItem menuItem = menuItemService.findOrFail(id);
        Ultis.throwUnless(menuItemPolicy.canDelete(JwtGuard.userPrincipal(), menuItem), new ForbiddenException());

        menuItemService.deleteMenuItem(menuItem);
        return ApiResponseEntity.success("Xóa mục trong thực đơn thành công");
    }
}