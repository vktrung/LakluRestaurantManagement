package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.ReservationPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservation;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.ReservationService;
import com.laklu.pos.uiltis.Ultis;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reservations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationController {

    ReservationService reservationService;
    ReservationPolicy reservationPolicy;

    @PostMapping("/")
    public ApiResponseEntity store(@Valid @RequestBody ReservationRequest request) throws Exception {
        Ultis.throwUnless(reservationPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Reservation reservation = reservationService.createReservation(request);

        return ApiResponseEntity.success(reservation);
    }


    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UpdateReservationRequest request) throws Exception {
        Reservation reservation = reservationService.findOrFail(id);

        Ultis.throwUnless(reservationPolicy.canEdit(JwtGuard.userPrincipal(), reservation), new ForbiddenException());

        Reservation updatedReservation = reservationService.updateReservation(id, request);

        return ApiResponseEntity.success(updatedReservation);
    }
}