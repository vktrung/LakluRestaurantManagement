package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.SchedulePolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.dataObjects.response.ScheduleResponse;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.ScheduleService;
import com.laklu.pos.uiltis.Ultis;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final SchedulePolicy schedulePolicy;
    private final ScheduleService scheduleService;

    // Lấy danh sách lịch làm việc
    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(schedulePolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        // TODO: Thêm phân trang, DTO response
        List<Schedule> schedules = scheduleService.getAllSchedules();

        return ApiResponseEntity.success(schedules);
    }

    // Tạo mới lịch làm việc
    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewSchedule newSchedule) throws Exception {
        Ultis.throwUnless(schedulePolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Schedule createdSchedule = scheduleService.storeSchedule(newSchedule);

        return ApiResponseEntity.success(new ScheduleResponse(createdSchedule));
    }

    // Cập nhật lịch làm việc theo ID
    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Long id, @RequestBody @Validated NewSchedule newSchedule) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canEdit(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        Schedule updatedSchedule = scheduleService.editSchedule(id, newSchedule);

        return ApiResponseEntity.success(new ScheduleResponse(updatedSchedule));
    }

    // Lấy chi tiết lịch làm việc theo ID
    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable Long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canView(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        return ApiResponseEntity.success(new ScheduleResponse(schedule));
    }

    // Xóa lịch làm việc theo ID
    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable Long id) throws Exception {
        var schedule = scheduleService.findOrFail(id);

        Ultis.throwUnless(schedulePolicy.canDelete(JwtGuard.userPrincipal(), schedule), new ForbiddenException());

        scheduleService.deleteSchedule(id);

        return ApiResponseEntity.success("Xóa lịch làm việc thành công");
    }
}

