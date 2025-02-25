package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MenuItemResponse {
    private Integer id;
    private Integer dishId;
    private Integer menuId;
    private Long categoryId;
    private BigDecimal price;
    private Integer discount;
    private String status;

    public static MenuItemResponse fromEntity(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getDish().getId(),
                menuItem.getMenu().getId(),
                menuItem.getCategory().getId(),
                menuItem.getPrice(),
                menuItem.getDiscount(),
                menuItem.getStatus()
        );
    }
}