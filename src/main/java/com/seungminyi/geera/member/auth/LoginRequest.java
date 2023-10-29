package com.seungminyi.geera.member.auth;

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
