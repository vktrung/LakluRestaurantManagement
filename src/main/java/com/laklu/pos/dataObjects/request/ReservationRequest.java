package com.laklu.pos.dataObjects.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequest {
    String customerName;
    String customerPhone;
    List<Integer> tableIds; // Danh sách ID bàn muốn đặt
}
