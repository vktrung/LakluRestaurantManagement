package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.MenuPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewMenu;
import com.laklu.pos.dataObjects.response.DishResponse;
import com.laklu.pos.dataObjects.response.MenuItemResponse;
import com.laklu.pos.dataObjects.response.MenuResponse;
import com.laklu.pos.entities.Dish;
import com.laklu.pos.entities.Menu;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.MenuMapper;
import com.laklu.pos.repositories.MenuRepository;
import com.laklu.pos.services.AttachmentService;
import com.laklu.pos.services.MenuService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.MenuNameMustBeUnique;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "Menu Controller", description = "Quản lý thông tin thực đơn")
@AllArgsConstructor
public class MenuController {

    private final MenuPolicy menuPolicy;
    private final MenuService menuService;
    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final AttachmentService attachmentService;

    @Operation(summary = "Lấy thông tin tất cả thực đơn", description = "API này dùng để lấy danh sách tất cả thực đơn")
    @GetMapping("/")
    public ApiResponseEntity getAllMenus() throws Exception {
        Ultis.throwUnless(menuPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<MenuResponse> menus = menuService.getAll().stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());

        return ApiResponseEntity.success(menus);
    }

    @Operation(summary = "Tạo một thực đơn mới", description = "API này dùng để tạo một thực đơn mới")
    @PostMapping("/")
    public ApiResponseEntity createMenu(@RequestBody NewMenu newMenu) throws Exception {
        Ultis.throwUnless(menuPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        validateMenuName(newMenu.getName());

        Menu menu = new Menu();
        menu.setName(newMenu.getName());
        menu.setStartAt(newMenu.getStartAt());
        menu.setEndAt(newMenu.getEndAt());
        menu.setStatus(Menu.MenuStatus.ENABLE);

        Menu createdMenu = menuService.createMenu(menu);
        return ApiResponseEntity.success(MenuResponse.fromEntity(createdMenu));
    }

    @Operation(summary = "Lấy thông tin thực đơn theo ID", description = "API này dùng để lấy thông tin thực đơn theo ID")
    @GetMapping("/{id}")
    public ApiResponseEntity getMenuById(@PathVariable Integer id) throws Exception {
        Menu menu = menuService.findOrFail(id);
        Ultis.throwUnless(menuPolicy.canView(JwtGuard.userPrincipal(), menu), new ForbiddenException());

        MenuResponse menuResponse = MenuResponse.fromEntity(menu);

        menuResponse.setMenuItems(menu.getMenuItems().stream().map((menuItem)-> {
            Dish dish = menuItem.getDish();
            DishResponse dishResponse = DishResponse.fromEntity(dish);
            dishResponse.setImages(dish.getAttachments().stream().map(attachmentService::toPersistAttachmentResponse).collect(Collectors.toList()));
            MenuItemResponse menuItemResponse = MenuItemResponse.fromEntity(menuItem);
            menuItemResponse.setDish(dishResponse);
            return menuItemResponse;
        }).collect(Collectors.toList()));

        return ApiResponseEntity.success(MenuResponse.fromEntity(menu));
    }

    @Operation(summary = "Cập nhật một phần thông tin thực đơn", description = "API này dùng để cập nhật một phần thông tin thực đơn theo ID")
    @PutMapping("/{id}")
    public ApiResponseEntity partialUpdateMenu(@PathVariable Integer id, @RequestBody NewMenu partialUpdateMenu) throws Exception {
        Menu existingMenu = menuService.findOrFail(id);
        Ultis.throwUnless(menuPolicy.canEdit(JwtGuard.userPrincipal(), existingMenu), new ForbiddenException());

        //validateMenuName(partialUpdateMenu.getName());

        menuMapper.updateMenuFromDto(partialUpdateMenu, existingMenu);

        Menu updatedMenu = menuService.updateMenu(existingMenu);
        return ApiResponseEntity.success(MenuResponse.fromEntity(updatedMenu));
    }

    @Operation(summary = "Xóa thực đơn theo ID", description = "API này dùng để xóa thực đơn")
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteMenu(@PathVariable Integer id) throws Exception {
        Menu menu = menuService.findOrFail(id);
        Ultis.throwUnless(menuPolicy.canDelete(JwtGuard.userPrincipal(), menu), new ForbiddenException());

        menuService.deleteMenu(menu);
        return ApiResponseEntity.success("Xóa thực đơn thành công");
    }

    private void validateMenuName(String name) {
        MenuNameMustBeUnique rule = new MenuNameMustBeUnique(menuRepository::findByName, name);
        if (!rule.isValid()) {
            throw new IllegalArgumentException(rule.getMessage());
        }
    }
}