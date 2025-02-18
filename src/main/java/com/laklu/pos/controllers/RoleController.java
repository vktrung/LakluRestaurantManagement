package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.RolePolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewRole;
import com.laklu.pos.dataObjects.request.UpdateRole;
import com.laklu.pos.dataObjects.response.RoleResource;
import com.laklu.pos.dataObjects.response.RoleResponse;
import com.laklu.pos.entities.Role;
import com.laklu.pos.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {

    private final RolePolicy rolePolicy;
    private final RoleService roleService;

    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewRole role) {
        rolePolicy.canCreate(JwtGuard.userPrincipal());

        Role persitedRole = roleService.storeRole(role);

        return ApiResponseEntity.success(new RoleResource(persitedRole));
    }

    @GetMapping("/")
    public ApiResponseEntity index() {
        rolePolicy.canList(JwtGuard.userPrincipal());

        List<Role> roles = roleService.getAllRoles();

        return ApiResponseEntity.success(roles.stream().map(RoleResource::new).toList());
    }

    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable int id) {
        Role role = roleService.findOrFail(id);

        rolePolicy.canView(JwtGuard.userPrincipal(), role);

        return ApiResponseEntity.success(new RoleResource(role));
    }

    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable int id, @RequestBody @Validated UpdateRole updateRole) {
        Role roleToUpdate = roleService.findOrFail(id);

        rolePolicy.canEdit(JwtGuard.userPrincipal(), roleToUpdate);

        Role updatedRole = roleService.updateRole(updateRole, roleToUpdate);

        return ApiResponseEntity.success(new RoleResource(updatedRole));
    }

    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable int id) {
        Role role = roleService.findOrFail(id);

        rolePolicy.canDelete(JwtGuard.userPrincipal(), role);

        roleService.deleteRole(role);

        return ApiResponseEntity.success("Xóa role thành công");
    }

    @GetMapping("/allroleswithusers")
    public ApiResponseEntity listRole() {
        rolePolicy.canList(JwtGuard.userPrincipal());

        List<RoleResponse> roles = roleService.getAllRoleWithUsersCount();

        return ApiResponseEntity.success(roles);
    }
}
