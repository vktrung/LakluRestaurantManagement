package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.dtos.MenuItemDTO;
import com.laklu.pos.dataObjects.reponse.ResponseData;
import com.laklu.pos.dataObjects.reponse.ResponseError;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.services.MenuItemService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-item")
@Validated
@Slf4j
public class MenuItemController {
    private final MenuItemService menuService;

    public MenuItemController(MenuItemService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseData<MenuItemDTO> add(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO savedMenu = menuService.addMenu(menuItemDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Thêm món ăn thành công", savedMenu);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi thêm món ăn");
        }
    }

    @GetMapping
    public ResponseData<List<MenuItemDTO>> get() {
        try {
            List<MenuItemDTO> menus = menuService.getAllMenus();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách món ăn thành công", menus);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy danh sách món ăn");
        }
    }

    @PutMapping("/{id}")
    public ResponseData<MenuItemDTO> update(@PathVariable int id, @Valid @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO updatedMenu = menuService.updateMenu(id, menuItemDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật món ăn thành công", updatedMenu);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật món ăn");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> delete(@PathVariable int id) {
        try {
            menuService.deleteMenu(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Xóa món ăn thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi xóa món ăn");
        }
    }
}
