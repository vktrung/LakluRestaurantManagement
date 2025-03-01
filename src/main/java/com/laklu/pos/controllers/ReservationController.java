package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.ReservationPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.ReservationTableUpdateDTO;
import com.laklu.pos.dataObjects.request.UpdateReservationDTO;
import com.laklu.pos.entities.Reservation;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Table;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.services.ReservationService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.ValidationRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Operation(summary = "Tạo đặt bàn", description = "API này dùng để thu thập thông tin khách hàng")
    @PostMapping("/")
    public ApiResponseEntity store(@Valid @RequestBody ReservationRequest request) throws Exception {
        Ultis.throwUnless(reservationPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        request.setReservationTime(LocalDateTime.now());

        List<Table> tables = tableRepository.findAllById(request.getTableIds());
        // get table seat capacity
        Integer totalSeat = tables.stream().map(Table::getCapacity).reduce(0, Integer::sum);
        var peopleMustBeSuitable = new ValidationRule(
                (v) -> request.getNumberOfPeople() <= totalSeat,
                "numberOfPeople",
                "Số người không được vượt quá số chỗ của bàn"
        );

        RuleValidator.validate(peopleMustBeSuitable);

        Reservation reservation = reservationService.createReservation(request);

        return ApiResponseEntity.success(reservation);
    }

    @Operation(summary = "Cập nhật thông tin đặt bàn", description = "API này dùng để cập nhật thông tin đặt bàn")
    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UpdateReservationDTO request) throws Exception {
        Reservation reservation = reservationService.findOrFail(id);

        Ultis.throwUnless(reservationPolicy.canEdit(JwtGuard.userPrincipal(), reservation), new ForbiddenException());

        List<Table> tables = tableRepository.findAllById(request.getTableIds());

        Integer totalSeat = tables.stream().map(Table::getCapacity).reduce(0, Integer::sum);
        var peopleMustBeSuitable = new ValidationRule(
                (v) -> request.getNumberOfPeople() <= totalSeat,
                "numberOfPeople",
                "Số người không được vượt quá số chỗ của bàn"
        );

        RuleValidator.validate(peopleMustBeSuitable);

        Reservation updatedReservation = reservationService.updateReservationInfo(reservation, request);

        return ApiResponseEntity.success(updatedReservation);
    }

    @Operation(summary = "Thêm bàn vào đặt bàn", description = "API này dùng để thêm bàn vào đặt bàn")
    @PostMapping("/{reservation_id}/tables")
    public ApiResponseEntity update(@PathVariable("reservation_id") Integer id, @Valid @RequestBody ReservationTableUpdateDTO request) throws Exception {
        Reservation reservation = reservationService.findOrFail(id);

        Ultis.throwUnless(reservationPolicy.canEdit(JwtGuard.userPrincipal(), reservation), new ForbiddenException());

        Reservation updatedReservation = reservationService.addTablesToReservation(reservation, request.getTableIds());

        return ApiResponseEntity.success(updatedReservation);
    }

    @Operation(summary = "Xóa bàn khỏi đặt bàn", description = "API này dùng để xóa bàn khỏi đặt bàn")
    @DeleteMapping("/{reservation_id}/tables")
    public ApiResponseEntity deleteTables(@PathVariable("reservation_id") Integer id, @Valid @RequestBody ReservationTableUpdateDTO request) throws Exception {
        Reservation reservation = reservationService.findOrFail(id);

        Ultis.throwUnless(reservationPolicy.canEdit(JwtGuard.userPrincipal(), reservation), new ForbiddenException());

        reservationService.deleteTablesReservation(reservation, request.getTableIds());

        return ApiResponseEntity.success("Xóa bàn thành công");
    }
}