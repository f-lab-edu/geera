package com.seungminyi.geera.member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponse {
    private final String token;
    private final String username;
}
