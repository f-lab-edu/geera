package com.seungminyi.geera.member.auth;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String Password;
}
