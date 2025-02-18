package com.laklu.pos.controllers;

import com.laklu.pos.auth.PermissionAlias;
import com.laklu.pos.entities.Permission;
import com.laklu.pos.entities.Role;
import com.laklu.pos.entities.User;
import com.laklu.pos.repositories.PermissionRepository;
import com.laklu.pos.repositories.RoleRepository;
import com.laklu.pos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Permissions;
import java.util.*;

@RestController
@RequestMapping("/api/v1/setup")
public class SetupController {

    @Value("${app-setup.adminUsername}")
    private String adminUsername;
    @Value("${app-setup.adminPassword}")
    private String adminPassword;
    @Value("${app-setup.adminEmail}")
    private String adminEmail;

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SetupController(PermissionRepository permissionRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/permissions")
    public String setUpPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        var declaredPermissions = PermissionAlias.values();
        if (permissions.size() == declaredPermissions.length) {
            return "Permissions already set up";
        }
        List<Permission> newPermission = new ArrayList<>();

        Arrays.stream(declaredPermissions).forEach(permissionAlias -> {
            if (permissions.stream().noneMatch(permission -> permission.getAlias().equals(permissionAlias.getAlias()))) {
                Permission permission = new Permission();
                permission.setAlias(permissionAlias.getAlias());
                permission.setName(permissionAlias.getName());
                permission.setPermissionGroup(permissionAlias.getGroup());
                newPermission.add(permission);
            }
        });

        permissionRepository.saveAll(newPermission);
        return "Setup permissions";
    }

    @GetMapping("/super-admin")
    public String setUpSuperAdmin() {
        Role role = new Role();
        role.setName("Quản trị viên hệ thống");
        role.setDescription("Quản trị viên hệ thống");
        role.setPermissions(new HashSet<>(permissionRepository.findAll()));
        roleRepository.save(role);

        User user = new User();
        user.setUsername(adminUsername);
        user.setPassword(adminPassword, passwordEncoder);
        user.setEmail(adminEmail);
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        userRepository.save(user);

        return "Setup super admin";
    }


}
