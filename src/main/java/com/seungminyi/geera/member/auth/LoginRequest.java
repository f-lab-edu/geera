package com.seungminyi.geera.member.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class LoginRequest {
    @NotNull
    String email;
    @NotNull
    String password;
}
