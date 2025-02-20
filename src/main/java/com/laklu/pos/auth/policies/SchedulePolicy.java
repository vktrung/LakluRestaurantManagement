package com.laklu.pos.auth.policies;

import com.laklu.pos.auth.PermissionAlias;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.valueObjects.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class SchedulePolicy implements Policy<Schedule> {
    @Override
    public boolean canCreate(UserPrincipal userPrincipal) {
        return userPrincipal.hasPermission(PermissionAlias.CREATE_SCHEDULE);
    }

    @Override
    public boolean canEdit(UserPrincipal userPrincipal, Schedule schedule) {
        return userPrincipal.hasPermission(PermissionAlias.UPDATE_SCHEDULE);
    }

    @Override
    public boolean canDelete(UserPrincipal userPrincipal, Schedule schedule) {
        return userPrincipal.hasPermission(PermissionAlias.DELETE_SCHEDULE);
    }

    @Override
    public boolean canView(UserPrincipal userPrincipal, Schedule schedule) {
        return userPrincipal.hasPermission(PermissionAlias.LIST_SCHEDULE);
    }

    @Override
    public boolean canList(UserPrincipal userPrincipal) {
        return userPrincipal.hasAnyPermission(PermissionAlias.LIST_SCHEDULE);
    }
}
