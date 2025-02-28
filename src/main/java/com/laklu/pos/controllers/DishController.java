package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.DishPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewDish;
import com.laklu.pos.dataObjects.response.DishResponse;
import com.laklu.pos.entities.Dish;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.DishMapper;
import com.laklu.pos.services.DishService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.ValueExistIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/dishes")
@Tag(name = "Dish Controller", description = "Quản lý thông tin món ăn")
@AllArgsConstructor
public class DishController {

    private final DishPolicy dishPolicy;
    private final DishService dishService;
    private final DishMapper dishMapper;

    @Operation(summary = "Lấy thông tin tất cả món ăn", description = "API này dùng để lấy danh sách tất cả món ăn")
    @GetMapping("/")
    public ApiResponseEntity getAllDishes() throws Exception {
        Ultis.throwUnless(dishPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<DishResponse> dishes = dishService.getAll().stream()
                .map(DishResponse::fromEntity)
                .collect(Collectors.toList());

        return ApiResponseEntity.success(dishes);
    }

    @Operation(summary = "Tạo một món ăn mới", description = "API này dùng để tạo một món ăn mới")
    @PostMapping("/")
    public ApiResponseEntity createDish(@RequestBody NewDish newDish) throws Exception {
        Ultis.throwUnless(dishPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        this.validateName(newDish.getName());

        Dish dish = new Dish();
        dish.setName(newDish.getName());
        dish.setDescription(newDish.getDescription());

        Dish createdDish = dishService.createDish(dish);

        return ApiResponseEntity.success(createdDish);
    }

    @Operation(summary = "Lấy thông tin món ăn theo ID", description = "API này dùng để lấy thông tin món ăn theo ID")
    @GetMapping("/{id}")
    public ApiResponseEntity getDishById(@PathVariable Integer id) throws Exception {
        Dish dish = dishService.findOrFail(id);

        Ultis.throwUnless(dishPolicy.canView(JwtGuard.userPrincipal(), dish), new ForbiddenException());

        return ApiResponseEntity.success(DishResponse.fromEntity(dish));
    }

    @Operation(summary = "Cập nhật thông tin món ăn", description = "API này dùng để cập nhật thông tin món ăn theo ID")
    @PutMapping("/{id}")
    public ApiResponseEntity partialUpdateDish(@PathVariable Integer id, @RequestBody NewDish partialUpdateDish) throws Exception {
        Dish existingDish = dishService.findOrFail(id);

        Ultis.throwUnless(dishPolicy.canEdit(JwtGuard.userPrincipal(), existingDish), new ForbiddenException());

        //this.validateName(partialUpdateDish.getName());

        dishMapper.updateDishFromDto(partialUpdateDish, existingDish);

        Dish updatedDish = dishService.updateDish(existingDish);

        return ApiResponseEntity.success(DishResponse.fromEntity(updatedDish));
    }

    @Operation(summary = "Xóa món ăn theo ID", description = "API này dùng để xóa món ăn")
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteDish(@PathVariable Integer id) throws Exception {
        Dish dish = dishService.findOrFail(id);

        Ultis.throwUnless(dishPolicy.canDelete(JwtGuard.userPrincipal(), dish), new ForbiddenException());

        dishService.deleteDish(dish);

        return ApiResponseEntity.success("Xóa món ăn thành công");
    }

    private void validateName(String name) {
        ValueExistIn<String> rule = new ValueExistIn<>(
                "Món ăn",
                name,
                (n) -> dishService.findByName(n).isEmpty()
        );
        if (!rule.isValid()) {
            throw new IllegalArgumentException(rule.getMessage());
        }
    }
}