package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUser {
    private String email;
    private String phone;
    private List<Integer> roleIds;
    private Integer salaryRateId;
}
