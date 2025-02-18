package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.TablePolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewTable;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.dataObjects.response.TableResponse;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.TableService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustBeUnique;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("api/v1/tables")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableController {
    TableService tableService;
    TablePolicy tablePolicy;

    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(tablePolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<Tables> tables = tableService.getAllTables();

        return ApiResponseEntity.success(tables);
    }

    @PostMapping("/")
    public ApiResponseEntity store(@Valid @RequestBody NewTable request) throws Exception{
        Ultis.throwUnless(tablePolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Function<String, Optional<Tables>> tabeResolver = tableService::findByTableName;

        RuleValidator.validate(new TableMustBeUnique(tabeResolver, request.getTableNumber()));

        Tables tables = tableService.createTable(request);

        return ApiResponseEntity.success(new TableResponse(tables));
    }

    @GetMapping("/{id}")
    public ApiResponseEntity show(@PathVariable Integer id) throws Exception {
        Tables tables = tableService.findOrFail(id);

        Ultis.throwUnless(tablePolicy.canView(JwtGuard.userPrincipal(), tables), new ForbiddenException());


        return ApiResponseEntity.success(new TableResponse(tables));
    }

    @PutMapping("/{id}")
    public ApiResponseEntity update(@PathVariable Integer id, @Valid @RequestBody TableUpdateRequest request) throws Exception {
        Tables tables = tableService.findOrFail(id);

        Ultis.throwUnless(tablePolicy.canEdit(JwtGuard.userPrincipal(), tables), new ForbiddenException());

        Tables updatedTables = tableService.updateTable(id, request);

        return ApiResponseEntity.success(new TableResponse(updatedTables));
    }

    @DeleteMapping("/{id}")
    public ApiResponseEntity delete(@PathVariable Integer id) throws Exception {
        Ultis.throwUnless(tablePolicy.canDelete(JwtGuard.userPrincipal(), tableService.findOrFail(id)), new ForbiddenException());

        tableService.deleteTable(id);

        return ApiResponseEntity.success("Table deleted successfully.");
    }
}
