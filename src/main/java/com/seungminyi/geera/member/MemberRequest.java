package com.seungminyi.geera.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seungminyi.geera.utill.validator.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
public class MemberRequest {
    @Email
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    private String password;
    @NotNull
    private String name;
    @NotNull @JsonProperty("security_code")
    private String securityCode;
}
