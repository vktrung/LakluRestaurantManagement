package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Role;
import com.laklu.pos.entities.SalaryRate;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponse {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private Set<String> roles;
    private String nameSalaryRate;

    public UserResponse(int id, String username, String email, String phone, String avatar, Set<Role> roles, String nameSalaryRate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.roles = roles.stream().map(Role::getName).collect(Collectors.toSet());
        this.nameSalaryRate = nameSalaryRate;
    }
}
