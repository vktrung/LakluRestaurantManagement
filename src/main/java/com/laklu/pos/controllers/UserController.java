package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.PolicyAuthorize;
import com.laklu.pos.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final PolicyAuthorize<User> userPolicy;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('users:read')")
    public String list() {
        userPolicy.list(JwtGuard.userPrincipal());

        return "Hello World!";
    }
}
