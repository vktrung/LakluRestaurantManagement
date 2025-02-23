package com.laklu.pos.services;

import com.laklu.pos.repositories.ActivityLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.laklu.pos.entities.ActivityLog;

@Service
@AllArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    // Phương thức ghi log vào bảng activity_logs
    public void logActivity(String staffId, String action, String target, String targetId, String details) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID cannot be null.");
        }
        ActivityLog activityLog = new ActivityLog();
        activityLog.setStaffId(Integer.parseInt(staffId));
        activityLog.setAction(action);
        activityLog.setTarget(target);
        activityLog.setTargetId(targetId.toString());
        activityLog.setDetails(details);
        activityLog.setCreatedAt(java.time.LocalDateTime.now());  // Lưu thời gian hiện tại

        // Lưu vào cơ sở dữ liệu
        activityLogRepository.save(activityLog);
    }
}