package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableCreationRequest {
    @NotBlank(message = "Table number is required") // Không được null hoặc rỗng
    @Size(max = 50, message = "Table number must not exceed 50 characters") // Giới hạn độ dài
    String tableNumber;

    @NotNull(message = "Capacity is required") // Không được null
    @Min(value = 1, message = "Capacity must be at least 1") // Giá trị tối thiểu là 1
    @Max(value = 20, message = "Capacity must not exceed 100") // Giới hạn số lượng tối đa
    Integer capacity;
}
