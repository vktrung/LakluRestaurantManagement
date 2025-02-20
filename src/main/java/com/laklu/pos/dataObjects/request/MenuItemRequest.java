package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    @NotBlank(message = "Yêu cầu nhập tên món ăn")
    private String name;
    private String description;
    @NotNull(message = "Yêu cầu nhập giá")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @Max(value = 1000000, message = "Giá không được vượt quá 1.000.000")
    private Double price;
    @NotNull(message = "Yêu cầu nhập id danh mục")
    private Long categoryId;
}
