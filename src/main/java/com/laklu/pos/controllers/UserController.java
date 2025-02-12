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
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final Policy<User> userPolicy;
    private final UserService userService;

    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(userPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());;
        List<User> users = userService.getAll();
        // todo: using pagination
        return ApiResponseEntity.success(users);
    }

    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody @Validated NewUser user) throws Exception {
        // authorize create user
        Ultis.throwUnless(userPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        // validate username must be unique
        Function<String, Optional<User>> userResolver = userService::findByUsername;
        RuleValidator.validate(new UsernameMustBeUnique(userResolver, user.getUsername()));

        // create user
        User persitedUser = userService.store(user);

        return ApiResponseEntity.success(new AuthUserResponse(new UserPrincipal(persitedUser)));
    }


}
