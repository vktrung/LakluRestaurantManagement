package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.Policy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewUser;
import com.laklu.pos.dataObjects.request.UpdateUser;
import com.laklu.pos.dataObjects.response.AuthUserResponse;
import com.laklu.pos.dataObjects.response.UserResponse;
import com.laklu.pos.entities.User;
import com.laklu.pos.entities.SalaryRate;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.UserMapper;
import com.laklu.pos.services.SalaryRateService;
import com.laklu.pos.services.UserService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.UsernameMustBeUnique;
import com.laklu.pos.valueObjects.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "Quản lý thông tin người dùng")
@AllArgsConstructor
public class UserController {

    private final Policy<User> userPolicy;
    private final UserService userService;
    private final UserMapper userMapper;
    private final SalaryRateService salaryRateService;

    @Operation(summary = "Lấy thông tin tất cả người dùng", description = "API này dùng để lấy thông tin tất cả nhân viên")
    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(userPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<UserResponse> users = userService.getAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getAvatar(),
                        user.getRoles(),
                        user.getSalaryRate().getLevelName()
                ))
                .toList();

        return ApiResponseEntity.success(users);
    }

    @Operation(summary = "Tạo một người dùng mới", description = "API này dùng để tạo một người dùng mới")
    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewUser user) throws Exception {
        System.out.println("Received user: " + user);
        Ultis.throwUnless(userPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Function<String, Optional<User>> userResolver = userService::findByUsername;

        RuleValidator.validate(new UsernameMustBeUnique(userResolver, user.getUsername()));

        User persitedUser = userService.store(user);

        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(persitedUser)));
    }

    // TODO: add update user with assign role
    @Operation(summary = "Lấy thông tin người dùng theo id", description = "API này dùng để lấy thông tin nhân viên theo id")
    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable int id) throws Exception {
        var user = userService.findOrFail(id);

        Ultis.throwUnless(userPolicy.canView(JwtGuard.userPrincipal(), user), new ForbiddenException());

        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(user)));
    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "API này dùng để cập nhật thông tin người dùng")
    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable int id, @RequestBody @Validated UpdateUser updateUser) throws Exception {
        var existingUser = userService.findOrFail(id);

        Ultis.throwUnless(userPolicy.canEdit(JwtGuard.userPrincipal(), existingUser), new ForbiddenException());

        userMapper.updateUserFromDto(updateUser, existingUser);

        if (updateUser.getSalaryRateId() != null) {
            SalaryRate salaryRate = salaryRateService.findOrFail(updateUser.getSalaryRateId());
            existingUser.setSalaryRate(salaryRate);
        }

        User updatedUser = userService.update(existingUser);
        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(updatedUser)));
    }

    @Operation(summary = "Xoá người dùng theo id", description = "API này dùng để xoá người dùng")
    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable int id) throws Exception {
        var user = userService.findOrFail(id);

        Ultis.throwUnless(userPolicy.canDelete(JwtGuard.userPrincipal(), user), new ForbiddenException());

        userService.deleteUser(user);

        return ApiResponseEntity.success("Xóa người dùng thành công");
    }
}
