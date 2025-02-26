package com.laklu.pos.dataObjects.response;

import com.laklu.pos.valueObjects.UserPrincipal;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class AuthUserResponse {
    private int id;
    private String username;
    private String email;
    private List<String> permissions;
    private String nameSalary;

    public AuthUserResponse(UserPrincipal userPrincipal) {
        this.id = userPrincipal.getPersitentUser().getId();
        this.username = userPrincipal.getPersitentUser().getUsername();
        this.email = userPrincipal.getPersitentUser().getEmail();
        this.permissions = userPrincipal.pluckPermissionAlias();

        if (userPrincipal.getPersitentUser().getSalaryRate() != null) {
            this.nameSalary = userPrincipal.getPersitentUser().getSalaryRate().getLevelName();
        } else {
            this.nameSalary = null; // or handle the null case as needed
        }
    }
}