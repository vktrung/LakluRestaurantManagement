package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.response.LogResponse;
import com.laklu.pos.dataObjects.response.UserInfo;
import com.laklu.pos.entities.ActivityLog;
import com.laklu.pos.entities.User;
import com.laklu.pos.services.ActivityLogService;
import com.laklu.pos.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/activity-logs")
@Tag(name = "Activity Log Controller", description = "Quản lý log hoạt động của người dùng")
public class ActivityLogController {

    ActivityLogService activityLogService;
    UserService userService;

    @Operation(summary = "Lấy danh sách log hoạt động", description = "API này dùng để lấy toàn bộ các log hoạt động của toàn bộ người dùng")
    @GetMapping("/")
    public ApiResponseEntity getAllActivityLogs() throws Exception {
        List<ActivityLog> activityLogs = activityLogService.getAllActivityLogs();
        List<LogResponse> logResponses = mapLog(activityLogs);
        return ApiResponseEntity.success(logResponses);
    }

    @Operation(summary = "Lấy danh sách log hoạt động theo ID người dùng", description = "API này dùng để lấy toàn bộ các log hoạt động của một người dùng")
    @GetMapping("/user/{userId}")
    public ApiResponseEntity getActivityLogsByUserId(@PathVariable Integer userId) throws Exception {
        List<ActivityLog> activityLogs = activityLogService.getActivityLogsByUserId(userId);
        List<LogResponse> logResponses = mapLog(activityLogs);
        return ApiResponseEntity.success(logResponses);
    }

    private List<LogResponse> mapLog(List<ActivityLog> activityLogs) {
        List<LogResponse> logResponses = activityLogs.stream()
                .map(this::convertToLogResponse)
                .collect(Collectors.toList());
        return logResponses;
    }

    private LogResponse convertToLogResponse(ActivityLog activityLog) {
        UserInfo userInfo = userService.getUserInfoById(activityLog.getStaffId());
        return new LogResponse(
                activityLog.getId(),
                activityLog.getStaffId(),
                activityLog.getAction().name(),
                activityLog.getTarget(),
                activityLog.getTargetId(),
                activityLog.getDetails(),
                activityLog.getDetails(),
                activityLog.getCreatedAt(),
                userInfo
        );
    }
}



