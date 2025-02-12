package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.*;
import com.laklu.pos.enums.StatusTable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableUpdateRequest {

    @NotBlank(message = "Table number is required") // Không được null hoặc rỗng
    @Size(max = 50, message = "Table number must not exceed 50 characters") // Giới hạn độ dài
    private String tableNumber;

    @NotNull(message = "Capacity is required") // Không được null
    @Min(value = 1, message = "Capacity must be at least 1") // Giá trị tối thiểu là 1
    @Max(value = 100, message = "Capacity must not exceed 100") // Giới hạn số lượng tối đa
    private Integer capacity;

    @NotNull(message = "Status is required") // Không được null
    private StatusTable status;
}
