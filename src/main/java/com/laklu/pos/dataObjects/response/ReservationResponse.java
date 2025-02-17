package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Reservation;
import lombok.AccessLevel;
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
    Reservation.Status status;
    LocalDateTime checkIn;
    LocalDateTime checkOut;
    List<Integer> tableIds;

    public ReservationResponse(Reservation reservation) {
        this.customerName = reservation.getCustomerName();
        this.customerPhone = reservation.getCustomerPhone();
        this.reservationTime = reservation.getReservationTime();
        this.status = reservation.getStatus();
        this.checkIn = reservation.getCheckIn();
        this.checkOut = reservation.getCheckOut();

        // Lấy danh sách tableIds từ reservationTables
        this.tableIds = reservation.getReservationTables() != null
                ? reservation.getReservationTables().stream()
                .map(rt -> rt.getTables().getId())
                .collect(Collectors.toList())
                : null;
    }
}
