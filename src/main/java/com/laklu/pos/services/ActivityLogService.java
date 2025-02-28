package com.laklu.pos.services;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.entities.ActivityLog;
import com.laklu.pos.enums.TrackedResourceType;
import com.laklu.pos.repositories.ActivityLogRepository;
import com.laklu.pos.entities.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void logActivity(Object entity, TrackedResourceType.Action action, String targetId, TrackedResourceType resourceType) {
        ActivityLog log = new ActivityLog();
        log.setStaffId(getCurrentStaffId());
        log.setTarget(entity.getClass().getSimpleName());
        log.setAction(action);
        log.setTargetId(targetId);
        log.setDetails(resourceType.getMessage(action));

        activityLogRepository.save(log);
    }

    private Integer getCurrentStaffId() {
        try {
            return JwtGuard.userPrincipal().getPersitentUser().getId();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to get user ID from JWT", e);
        }
    }

    public String getEntityId(Identifiable entity) {
            return String.valueOf( entity.getId() );
    }

    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll();
    }

    public List<ActivityLog> getActivityLogsByUserId(Integer userId) {
        return activityLogRepository.findByStaffId(userId);
    }
}


