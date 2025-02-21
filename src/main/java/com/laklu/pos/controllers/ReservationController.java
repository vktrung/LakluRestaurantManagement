package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.ReservationPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservation;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.repositories.ReservationTableRepository;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.services.ReservationService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Controller", description = "Quản lý thông tin đặt bàn")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationController {

    ReservationService reservationService;
    ReservationPolicy reservationPolicy;
    TableRepository tableRepository;
    ReservationTableRepository reservationTableRepository;

    @Operation(summary = "Tạo đặt bàn", description = "API này dùng để tạo đặt bàn mới")
    @PostMapping("/")
    public ApiResponseEntity store(@Valid @RequestBody ReservationRequest request) throws Exception {
        Ultis.throwUnless(reservationPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        request.setReservationTime(LocalDateTime.now());

        List<Tables> tables = tableRepository.findAllById(request.getTableIds());

        RuleValidator.validate(new TableMustAvailable(tables, reservationTableRepository, request.getCheckIn().toLocalDate()));

        Reservation reservation = reservationService.createReservation(request);

        return ApiResponseEntity.success(reservation);
    }

    @Operation(summary = "Cập nhật đặt bàn", description = "API này dùng để cập nhật thông tin đặt bàn")
    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UpdateReservationRequest request) throws Exception {
        Reservation reservation = reservationService.findOrFail(id);

        Ultis.throwUnless(reservationPolicy.canEdit(JwtGuard.userPrincipal(), reservation), new ForbiddenException());

        Reservation updatedReservation = reservationService.updateReservation(id, request);

        return ApiResponseEntity.success(updatedReservation);
    }
}