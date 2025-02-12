package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequest {
    @NotNull(message = "Customer name cannot be null")
    @Size(min = 3, max = 100, message = "Customer name must be between 3 and 100 characters")
    String customerName;

    @NotNull(message = "Customer phone cannot be null")
    @Size(min = 10, max = 15, message = "Customer phone must be between 10 and 15 digits")
    String customerPhone;

    @NotNull(message = "Reservation time cannot be null")
    LocalDateTime reservationTime;

    @NotEmpty(message = "At least one table must be selected")
    List<Integer> tableIds;
}
