package com.laklu.pos.dataObjects.response;

import com.laklu.pos.valueObjects.UserPrincipal;
import lombok.Data;

import java.util.List;

@Data
public class AuthUserResponse {
    private int id;
    private String username;
    private String email;
    private List<String> permissions;

    public AuthUserResponse(UserPrincipal userPrincipal) {
        this.id = userPrincipal.getPersitentUser().getId();
        this.username = userPrincipal.getPersitentUser().getUsername();
        this.email = userPrincipal.getPersitentUser().getEmail();
        this.permissions = userPrincipal.pluckPermissionAlias();
    }
}
