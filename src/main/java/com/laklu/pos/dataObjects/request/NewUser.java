package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewUser {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private List<Integer> roleIds;
    @NotNull
    private Integer salaryRateId;
}