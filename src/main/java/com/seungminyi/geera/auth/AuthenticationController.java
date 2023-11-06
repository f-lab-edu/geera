package com.seungminyi.geera.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.auth.dto.LoginRequest;
import com.seungminyi.geera.auth.dto.LoginResponse;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {
	private final AuthService authService;

	public AuthenticationController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/api/login")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
		try {
			String jwtToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
			return ResponseEntity.ok(new LoginResponse(jwtToken, loginRequest.getEmail()));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
