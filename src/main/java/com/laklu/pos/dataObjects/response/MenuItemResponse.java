package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.MenuItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemResponse {
    private Integer id;
    private Integer dishId;
    private Integer menuId;
    private Long categoryId;
    private BigDecimal price;
    private String status;
    private DishResponse dish;

    public MenuItemResponse(Integer id, Integer dishId, Integer menuId, Long categoryId, BigDecimal price, String status) {
        this.id = id;
        this.dishId = dishId;
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.price = price;
        this.status = status;
    }

    public static MenuItemResponse fromEntity(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getDish().getId(),
                menuItem.getMenu().getId(),
                menuItem.getCategory().getId(),
                menuItem.getPrice(),
                menuItem.getStatus()
        );
    }
}