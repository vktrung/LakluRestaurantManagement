package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Reservations;
import com.laklu.pos.entities.Tables;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    String customerName;
    String customerPhone;
    LocalDateTime reservationTime;
    Reservations.Status status;
    LocalDateTime checkIn;
    LocalDateTime checkOut;
    List<Integer> tableIds;

    public ReservationResponse(Reservations reservations) {
        this.customerName = reservations.getCustomerName();
        this.customerPhone = reservations.getCustomerPhone();
        this.reservationTime = reservations.getReservationTime();
        this.status = reservations.getStatus();
        this.checkIn = reservations.getCheckIn();
        this.checkOut = reservations.getCheckOut();

        // Lấy danh sách tableIds từ reservationTables
        this.tableIds = reservations.getReservationTables() != null
                ? reservations.getReservationTables().stream()
                .map(rt -> rt.getTable().getId())
                .collect(Collectors.toList())
                : null;
    }
}
