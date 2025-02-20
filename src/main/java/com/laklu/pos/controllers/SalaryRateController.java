package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.SalaryRatePolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.NewSalaryRate;
import com.laklu.pos.entities.SalaryRate;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.SalaryRateService;
import com.laklu.pos.uiltis.Ultis;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salary-rates")
@AllArgsConstructor
public class SalaryRateController {

    private final SalaryRateService salaryRateService;
    private final SalaryRatePolicy salaryRatePolicy;

    @GetMapping("/")
    public ApiResponseEntity index() throws Exception {
        Ultis.throwUnless(salaryRatePolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        List<SalaryRate> salaryRates = salaryRateService.getAll();
        return ApiResponseEntity.success(salaryRates);
    }

    @PostMapping("/")
    public ApiResponseEntity store(@RequestBody NewSalaryRate newSalaryRate) throws Exception {
        Ultis.throwUnless(salaryRatePolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        SalaryRate salaryRate = salaryRateService.createSalaryRate(newSalaryRate);
        return ApiResponseEntity.success(salaryRate);
    }

    @PutMapping("/{id}")
    public ApiResponseEntity updateSalaryRate(@PathVariable Integer id, @RequestBody NewSalaryRate newSalaryRate) throws Exception{
        var salaryRate = salaryRateService.findOrFail(id);
        Ultis.throwUnless(salaryRatePolicy.canEdit(JwtGuard.userPrincipal(), salaryRate), new ForbiddenException());
        SalaryRate updatedSalaryRate = salaryRateService.updateSalaryRate(id, newSalaryRate);
        return ApiResponseEntity.success(updatedSalaryRate);
    }

    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteSalaryRate(@PathVariable Integer id) {
        SalaryRate salaryRate = salaryRateService.findOrFail(id);
        salaryRateService.deleteSalaryRate(salaryRate);
        return ApiResponseEntity.success("Salary rate deleted successfully");
    }

}