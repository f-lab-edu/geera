package com.seungminyi.geera.member.dto;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seungminyi.geera.utill.validator.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Validated
@Data
public class MemberRequest {
	@Email
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Password
	private String password;
	@NotNull
	private String name;
	@NotNull
	@JsonProperty("security_code")
	private String securityCode;

	public Member toMember() {
		Member member = new Member();
		member.setEmail(this.email);
		member.setPassword(this.password);
		member.setName(this.name);
		return member;
	}
}
