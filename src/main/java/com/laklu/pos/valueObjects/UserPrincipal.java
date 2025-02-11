package com.laklu.pos.valueObjects;

import com.laklu.pos.auth.exceptions.RequirePermissionException;
import com.laklu.pos.auth.policies.RequireAuthorizePermission;
import com.laklu.pos.entities.Permission;
import com.laklu.pos.entities.Role;
import com.laklu.pos.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        return roles.stream()
                .map(Role::getPermissions)
                .flatMap(Set::stream)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }


    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public User getPersitentUser() {
        return this.user;
    }

    private List<String> pluckPermissionAlias() {
        return this.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean hasAnyPermission(String... permissions) {
        return Arrays.stream(permissions)
                .anyMatch(permission -> this.pluckPermissionAlias().contains(permission));
    }

    public boolean hasAllPermissions(String... permissions) {
        return Arrays.stream(permissions)
                .allMatch(permission -> this.pluckPermissionAlias().contains(permission));
    }

    public boolean hasPermission(String permission) {
        return this.pluckPermissionAlias().contains(permission);
    }

//    public boolean hasAnyPermission(RequireAuthorizePermission... permissions) {
//        boolean hasPermission = Arrays.stream(permissions)
//                .anyMatch(permission -> this.getAuthorities().contains(permission));
//        if (!hasPermission) {
//            throw new RequirePermissionException();
//        }
//        return true;
//    }
//
//    public boolean hasAllPermissions(RequireAuthorizePermission... permissions) {
//        boolean hasPermission = Arrays.stream(permissions)
//                .allMatch(permission -> this.getAuthorities().contains(permission));
//        if (!hasPermission) {
//            throw new RequirePermissionException();
//        }
//        return true;
//    }
//
//    public boolean hasPermission(RequireAuthorizePermission permission) {
//        Permission persitentPermission = this.getAuthorities().stream()
//                .filter(p -> p.getAuthority().equals(permission.getAuthority()))
//                .map(p -> (Permission) p)
//                .findFirst()
//                .orElse(null);
//        if (!hasPermission) {
//            throw new RequirePermissionException();
//        }
//        return true;
//    }

}
