package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.SchedulePolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.dataObjects.request.ScheduleCheckInRequest;
import com.laklu.pos.dataObjects.request.ScheduleCheckOutRequest;
import com.laklu.pos.dataObjects.response.ScheduleResponse;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.ScheduleService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.valueObjects.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/schedule")
@Tag(name = "Schedule Controller", description = "Quản lý lịch làm việc")
public class ScheduleController {

    private final SchedulePolicy schedulePolicy;
    private final ScheduleService scheduleService;

    // Lấy danh sách lịch làm việc
    @Operation(summary = "Lấy danh sách tất cả lịch làm việc", description = "API này dùng để lấy toàn bộ các bạn của quán")
    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(schedulePolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<Schedule> schedules = scheduleService.getAllSchedules();

        return ApiResponseEntity.success(schedules);
    }

    // Tạo mới lịch làm việc
    @Operation(summary = "Tạo lịch làm việc", description = "API này dùng để tạo lịch làm việc mới")
    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewSchedule newSchedule) throws Exception {
        Ultis.throwUnless(schedulePolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Schedule createdSchedule = scheduleService.storeSchedule(newSchedule);

        return ApiResponseEntity.success(new ScheduleResponse(createdSchedule));
    }

    // Cập nhật lịch làm việc theo ID
    @Operation(summary = "Cập nhật lịch làm việc", description = "API này dùng để cập nhật thông tin lịch làm việc")
    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Long id, @RequestBody @Validated NewSchedule newSchedule) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canEdit(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        Schedule updatedSchedule = scheduleService.editSchedule(schedule, newSchedule);

        return ApiResponseEntity.success(new ScheduleResponse(updatedSchedule));
    }

    // Lấy chi tiết lịch làm việc theo ID
    @Operation(summary = "Hiện thị lịch làm việc theo ID", description = "API này dùng để lấy thông tin lịch làm việc theo ID")
    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable Long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canView(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        return ApiResponseEntity.success(new ScheduleResponse(schedule));
    }

    // Xóa lịch làm việc theo ID
    @Operation(summary = "Xóa lịch làm việc theo ID", description = "API này dùng để xóa thông tin lịch làm việc theo ID")
    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable Long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canDelete(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        scheduleService.deleteSchedule(id);

        return ApiResponseEntity.success("Xóa lịch làm việc thành công");
    }

    // Tạo mã QR điểm danh
    @Operation(summary = "Tạo mã qr check in code", description = "API này dùng để tạo mã QR điểm danh")
    @PostMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/check-in-qr-code/{id}")
    public byte[] generateQRCheckInCode(@PathVariable("id") long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);
        Ultis.throwUnless(schedulePolicy.canCreateCheckInQrCode(JwtGuard.userPrincipal()), new ForbiddenException());
        return scheduleService.generateCheckInCode(schedule);
    }

    @Operation(summary = "Tạo mã qr check out code", description = "API này dùng để tạo mã QR điểm danh check out")
    @PostMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/check-out-qr-code/{id}")
    public byte[] generateQRCheckOutCode(@PathVariable("id") long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);
        Ultis.throwUnless(schedulePolicy.canCreateCheckOutQrCode(JwtGuard.userPrincipal()), new ForbiddenException());
        return scheduleService.generateCheckOutCode(schedule);
    }

    @PostMapping("/schedule-check-in")
    public ApiResponseEntity scheduleCheckIn(@RequestBody ScheduleCheckInRequest request) throws Exception {
        Schedule schedule = scheduleService.findOrFail(request.getScheduleId());
        UserPrincipal userPrincipal = scheduleService.getScheduleUser(request.getUsername(), request.getPassword());

        // Kiểm tra quyền check in
        Ultis.throwUnless(schedulePolicy.canCheckIn(userPrincipal, schedule), new ForbiddenException());
        scheduleService.validateCheckInCode(String.valueOf(request.getScheduleId()), request.getExpiry(), request.getSignature());
        scheduleService.createCheckInAttendance(schedule, userPrincipal.getPersitentUser());

        return ApiResponseEntity.success("Check in thành công");
    }


    @PostMapping("/schedule-check-out")
    public ApiResponseEntity scheduleCheckOut(@RequestBody ScheduleCheckOutRequest request) throws Exception {
        Schedule schedule = scheduleService.findOrFail(request.getScheduleId());
        UserPrincipal userPrincipal = scheduleService.getScheduleUser(request.getUsername(), request.getPassword());

        // Kiểm tra quyền check in
        Ultis.throwUnless(schedulePolicy.canCheckOut(userPrincipal, schedule), new ForbiddenException());
        scheduleService.validateCheckInCode(String.valueOf(request.getScheduleId()), request.getExpiry(), request.getSignature());
        scheduleService.checkOutAttendance(schedule, userPrincipal.getPersitentUser());

        return ApiResponseEntity.success("Check out thành công");
    }

}

