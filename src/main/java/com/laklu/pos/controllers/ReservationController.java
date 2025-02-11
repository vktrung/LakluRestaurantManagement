package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservations;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.services.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationController {

    ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservations> createReservation(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservations> updateReservation(
            @PathVariable Integer id,
            @RequestBody UpdateReservationRequest request) {

        Reservations updatedReservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(updatedReservation);
    }

}