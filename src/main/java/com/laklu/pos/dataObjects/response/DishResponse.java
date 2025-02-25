package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DishResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static DishResponse fromEntity(Dish dish) {
        return new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), dish.getCreatedAt() ,dish.getUpdatedAt());
    }
}
