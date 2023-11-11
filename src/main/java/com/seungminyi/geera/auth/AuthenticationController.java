package com.seungminyi.geera.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.auth.dto.LoginRequest;
import com.seungminyi.geera.auth.dto.LoginResponse;
import com.seungminyi.geera.common.dto.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "인증")
@RestController
public class AuthenticationController {
	private final AuthService authService;

	public AuthenticationController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "로그인")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "400", content = {
			@Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
	})
	@PostMapping("/api/login")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
		try {
			String jwtToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
			return ResponseEntity.ok(new LoginResponse(jwtToken, loginRequest.getEmail()));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
		}
	}
}
