package com.parqueadero.app.dtos.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class UserRequest {

    @NotBlank()
    @Size(min = 6, max = 50)
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    private List<@Valid RoleRequest> roles;
}
