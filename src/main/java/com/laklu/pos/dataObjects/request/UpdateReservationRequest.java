package com.laklu.pos.dataObjects.request;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationRequest {
    private String customerName;
    private String customerPhone;
    private LocalDateTime reservationTime;
    private List<Integer> tableIds;
}
