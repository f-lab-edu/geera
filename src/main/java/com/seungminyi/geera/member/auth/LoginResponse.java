package com.seungminyi.geera.member.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
	private String token;
	private String username;
}
