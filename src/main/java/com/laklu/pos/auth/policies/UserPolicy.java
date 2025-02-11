package com.laklu.pos.auth.policies;

import com.laklu.pos.entities.User;
import com.laklu.pos.valueObjects.UserPrincipal;

public class UserPolicy implements PolicyAuthorize<User> {
    @Override
    public boolean create(UserPrincipal userPrincipal, User user) {
        return userPrincipal.hasPermission("users:create");
    }

    @Override
    public boolean edit(UserPrincipal userPrincipal, User user) {
        return userPrincipal.hasPermission("users:edit");
    }

    @Override
    public boolean delete(UserPrincipal userPrincipal, User user) {
        return userPrincipal.hasPermission("users:delete");
    }

    @Override
    public boolean view(UserPrincipal userPrincipal, User user) {
        return userPrincipal.hasPermission("users:view");
    }

    @Override
    public boolean list(UserPrincipal userPrincipal) {
        return userPrincipal.hasPermission("users:list");
    }
}
