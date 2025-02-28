package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationDTO {
    @NotNull(message = "Không được để trống")
    @Size(min = 3, max = 100, message = "Tên phải nằm trong khoảng từ 3 đến 100 kí tự")
    String customerName;

    @NotNull(message = "Không được bỏ trống")
    @Size(min = 10, max = 15, message = "Số điện thoại gồm 10 chữ số")
    String customerPhone;

    @NotNull(message = "Không được bỏ trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "Thời gian đặt chỗ phải là hiện tại hoặc tương lai")
    LocalDateTime reservationTime;

    @NotEmpty(message = "Hãy chọn bàn")
    List<Integer> tableIds;

    @Min(value = 1, message = "Số lượng người phải lớn hơn 0")
    private Integer numberOfPeople;
}
