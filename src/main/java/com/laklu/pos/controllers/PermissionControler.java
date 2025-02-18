package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.UserPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.PermissionService;
import com.laklu.pos.uiltis.Ultis;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
@AllArgsConstructor
public class PermissionControler {

    private final PermissionService permissionService;
    private final UserPolicy userPolicy;

    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(userPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        return ApiResponseEntity.success(permissionService.getAll());
    }
}
