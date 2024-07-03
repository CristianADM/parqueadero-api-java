package com.parqueadero.app.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class UserRequest {

    @NotBlank
    @Size(min = 6, max = 50)
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
}
