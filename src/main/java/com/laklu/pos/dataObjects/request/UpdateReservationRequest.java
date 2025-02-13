package com.laklu.pos.dataObjects.request;

import lombok.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationRequest {

    @NotNull(message = "Tên khách hàng không được để trống")
    @Size(min = 3, max = 100, message = "Tên khách hàng phải từ 3 đến 100 ký tự")
    private String customerName;

    @NotNull(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 15, message = "Số điện thoại phải từ 10 đến 15 chữ số")
    private String customerPhone;

    @NotNull(message = "Thời gian đặt chỗ không được để trống")
    private LocalDateTime reservationTime;

    @NotEmpty(message = "Phải chọn ít nhất một bàn")
    private List<Integer> tableIds;
}
