package com.laklu.pos.auth.policies;

import com.laklu.pos.auth.exceptions.ForbiddenException;
import com.laklu.pos.valueObjects.UserPrincipal;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Policy<T> implements PolicyAuthorize<T> {

    private PolicyAuthorize<T> policyInterface;

    @Override
    public boolean create(UserPrincipal userPrincipal, T o) {
        return throwWhen(!policyInterface.create(userPrincipal, o));
    }

    @Override
    public boolean edit(UserPrincipal userPrincipal, T o) {
        return throwWhen(!policyInterface.edit(userPrincipal, o));
    }

    @Override
    public boolean delete(UserPrincipal userPrincipal, T o) {
        return throwWhen(!policyInterface.delete(userPrincipal, o));
    }

    @Override
    public boolean view(UserPrincipal userPrincipal, T o) {
        return throwWhen(!policyInterface.view(userPrincipal, o));
    }

    @Override
    public boolean list(UserPrincipal userPrincipal) {
        return throwWhen(!policyInterface.list(userPrincipal));
    }

    private boolean throwWhen(boolean condition) {
        if (condition) {
            throw new ForbiddenException();
        }
        return true;
    }
}
