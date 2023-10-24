package com.seungminyi.geera.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
public class Member {
    private String id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;
}
