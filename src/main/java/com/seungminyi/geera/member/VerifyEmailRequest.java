package com.seungminyi.geera.member;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Validated
@Getter
@Setter
public class VerifyEmailRequest {
	@Email
	@JsonProperty("email_address")
	private String emailAddress;
}
