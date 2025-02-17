package com.laklu.pos.dataObjects.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUser {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
}
