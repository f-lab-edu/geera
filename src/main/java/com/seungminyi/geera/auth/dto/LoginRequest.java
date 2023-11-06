package com.seungminyi.geera.auth.dto;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Validated
public class LoginRequest {
	@NotNull
	String email;
	@NotNull
	String password;
}
