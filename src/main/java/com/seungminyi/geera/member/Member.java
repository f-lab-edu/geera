package com.seungminyi.geera.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seungminyi.geera.utill.validator.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


@Getter @Setter
public class Member {
    private String id;
    @JsonIgnore
    private String password;
    private String name;
}
