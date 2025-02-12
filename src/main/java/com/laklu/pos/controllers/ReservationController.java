package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservations;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.services.ReservationService;
import jakarta.validation.Valid;
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
    public Reservations createReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.createReservation(request);
    }


    @PutMapping("/{id}")
    public Reservations updateReservation(
            @Valid
            @PathVariable Integer id,
            @RequestBody UpdateReservationRequest request) {

        return reservationService.updateReservation(id, request);
    }

}