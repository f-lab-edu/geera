package com.seungminyi.geera.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
	private String token;
	private String username;
	private String email;
}
