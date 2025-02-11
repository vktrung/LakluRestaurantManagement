package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.reponse.ResponseData;
import com.laklu.pos.dataObjects.reponse.ResponseError;
import com.laklu.pos.entities.Menu;
import com.laklu.pos.services.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Validated
@Slf4j
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Add a new menu item
    @PostMapping("/add")
    public ResponseData<Menu> addMenu(@RequestBody Menu menu) {
        if (menu.getName() == null || menu.getName().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Tên món ăn không được để trống");
        }
        if (menu.getDescription() == null || menu.getDescription().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mô tả không được để trống");
        }
        if (menu.getPrice() <= 0) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Giá món ăn phải lớn hơn 0");
        }
        if (menu.getQuantity() < 0) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Số lượng không được âm");
        }
        if (menu.getStatus() == null) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Trạng thái không được để trống");
        }
        if (menu.getCategory() == null) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Danh mục không được để trống");
        }

        try {
            Menu savedMenu = menuService.addMenu(menu);
            return new ResponseData<>(HttpStatus.OK.value(), "Thêm món ăn thành công", savedMenu);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi thêm món ăn");
        }
    }

    // Get all menu items
    @GetMapping
    public ResponseData<List<Menu>> getAllMenus() {
        try {
            List<Menu> menus = menuService.getAllMenus();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách món ăn thành công", menus);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy danh sách món ăn");
        }
    }

    // Update an existing menu item
    @PutMapping("/update/{id}")
    public ResponseData<Menu> updateMenu(@PathVariable int id, @RequestBody Menu menu) {
        if (menu.getName() == null || menu.getName().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Tên món ăn không được để trống");
        }
        if (menu.getDescription() == null || menu.getDescription().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mô tả không được để trống");
        }
        if (menu.getPrice() <= 0) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Giá món ăn phải lớn hơn 0");
        }
        if (menu.getQuantity() < 0) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Số lượng không được âm");
        }
        if (menu.getStatus() == null) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Trạng thái không được để trống");
        }
        if (menu.getCategory() == null) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Danh mục không được để trống");
        }

        try {
            Menu updatedMenu = menuService.updateMenu(id, menu);
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật món ăn thành công", updatedMenu);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật món ăn");
        }
    }

    // Delete a menu item
    @DeleteMapping("/delete/{id}")
    public ResponseData<Void> deleteMenu(@PathVariable int id) {
        try {
            menuService.deleteMenu(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Xóa món ăn thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi xóa món ăn");
        }
    }
}