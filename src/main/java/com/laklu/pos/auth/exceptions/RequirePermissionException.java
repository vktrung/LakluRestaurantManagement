package com.laklu.pos.auth.exceptions;

import com.laklu.pos.entities.Permission;

// TODO: beatify this class
public class RequirePermissionException extends RuntimeException {
    public RequirePermissionException(Permission permission) {
        super("You don't have permission to " + permission.getName());
    }

    public RequirePermissionException() {
        super("You don't have permission to perform this action");
    }
}
