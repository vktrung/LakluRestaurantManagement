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
    @NotNull(message = "Customer name cannot be null")
    @Size(min = 3, max = 100, message = "Customer name must be between 3 and 100 characters")
    private String customerName;

    @NotNull(message = "Customer phone cannot be null")
    @Size(min = 10, max = 15, message = "Customer phone must be between 10 and 15 digits")
    private String customerPhone;

    @NotNull(message = "Reservation time cannot be null")
    private LocalDateTime reservationTime;

    @NotEmpty(message = "At least one table must be selected")
    private List<Integer> tableIds;
}
