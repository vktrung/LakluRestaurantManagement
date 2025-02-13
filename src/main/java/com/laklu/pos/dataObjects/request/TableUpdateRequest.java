package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.*;
import com.laklu.pos.enums.StatusTable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableUpdateRequest {

    @NotBlank(message = "Số bàn là bắt buộc") // Không được null hoặc rỗng
    @Size(max = 50, message = "Số bàn không được vượt quá 50 ký tự") // Giới hạn độ dài
    private String tableNumber;

    @NotNull(message = "Sức chứa là bắt buộc") // Không được null
    @Min(value = 1, message = "Sức chứa phải ít nhất là 1") // Giá trị tối thiểu là 1
    @Max(value = 100, message = "Sức chứa không được vượt quá 100") // Giới hạn số lượng tối đa
    private Integer capacity;

    @NotNull(message = "Trạng thái bàn là bắt buộc") // Không được null
    private StatusTable status;
}

