package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.Policy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewUser;
import com.laklu.pos.dataObjects.response.AuthUserResponse;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
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
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Quản lý thông tin người dùng")
@AllArgsConstructor
public class UserController {

    private final Policy<User> userPolicy;
    private final UserService userService;

    @Operation(summary = "Lấy thông tin người dùng", description = "API này dùng để lấy thông tin tất cả nhân viên")
    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(userPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        // TODO: add pagination, and res dto
        List<User> users = userService.getAll();

        return ApiResponseEntity.success(users);
    }

    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewUser user) throws Exception {
        Ultis.throwUnless(userPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Function<String, Optional<User>> userResolver = userService::findByUsername;

        RuleValidator.validate(new UsernameMustBeUnique(userResolver, user.getUsername()));

        User persitedUser = userService.store(user);

        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(persitedUser)));
    }

    // TODO: add update user with assign role

    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable int id) throws Exception {
        var user = userService.findOrFail(id);

        Ultis.throwUnless(userPolicy.canView(JwtGuard.userPrincipal(), user), new ForbiddenException());

        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(user)));
    }


    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable int id) throws Exception {
        var user = userService.findOrFail(id);

        Ultis.throwUnless(userPolicy.canDelete(JwtGuard.userPrincipal(), user), new ForbiddenException());

        userService.deleteUser(user);

        return ApiResponseEntity.success("Xóa người dùng thành công");
    }
}
