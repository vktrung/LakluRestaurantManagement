package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.services.ActivityLogService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ActivityLoggingAspect {

    private final ActivityLogService activityLogService;

    // Định nghĩa pointcut cho các phương thức create, update, delete trong service
    @Pointcut("execution(* com.laklu.pos.services.*.create*(..)) || execution(* com.laklu.pos.services.*.update*(..)) || execution(* com.laklu.pos.services.*.delete*(..))")
    public void crudMethods() {}

    // Ghi lại hành động khi tạo đối tượng mới
    @Before("execution(* com.laklu.pos.services.*.create*(..))")
    public void logCreateActivity(JoinPoint joinPoint) {
        logActivity(joinPoint, "CREATE");
    }

    // Ghi lại hành động khi cập nhật đối tượng
    @Before("execution(* com.laklu.pos.services.*.update*(..))")
    public void logUpdateActivity(JoinPoint joinPoint) {
        logActivity(joinPoint, "UPDATE");
    }

    // Ghi lại hành động khi xóa đối tượng
    @Before("execution(* com.laklu.pos.services.*.delete*(..))")
    public void logDeleteActivity(JoinPoint joinPoint) {
        logActivity(joinPoint, "DELETE");
    }

    // Phương thức chung để ghi lại hành động
    private void logActivity(JoinPoint joinPoint, String action) {
        // Lấy ID người dùng hiện tại từ SecurityContext
        String staffId = getCurrentStaffId().toString();

        // Lấy tên đối tượng (class) và ID đối tượng từ tham số đầu tiên của phương thức
        String target = joinPoint.getSignature().getDeclaringTypeName(); // Tên class
        String targetId = (joinPoint.getArgs().length > 0) ? joinPoint.getArgs()[0].toString() : null; // ID đối tượng

        // Chi tiết hành động (tên phương thức)
        String details = joinPoint.getSignature().getName();

        // Ghi log hoạt động vào bảng activity_logs
        activityLogService.logActivity(staffId, action, target, targetId, details);
    }

    // Lấy ID của người dùng hiện tại (có thể từ SecurityContext)
    private Integer getCurrentStaffId() {
        try {
            // Lấy ID người dùng từ JWT
            return JwtGuard.userPrincipal().getPersitentUser().getId();
        } catch (Exception e) {
            // Nếu không thể lấy ID người dùng, có thể ném ngoại lệ hoặc trả về giá trị mặc định
            throw new IllegalStateException("Unable to get user ID from JWT", e);
        }
    }

}
