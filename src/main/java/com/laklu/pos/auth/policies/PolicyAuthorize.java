package com.laklu.pos.auth.policies;

import com.laklu.pos.valueObjects.UserPrincipal;

public interface PolicyAuthorize<T> {
    boolean create(UserPrincipal userPrincipal, T t);

    boolean edit(UserPrincipal userPrincipal, T t);

    boolean delete(UserPrincipal userPrincipal, T t);

    boolean view(UserPrincipal userPrincipal, T t);

    boolean list(UserPrincipal userPrincipal);
}
