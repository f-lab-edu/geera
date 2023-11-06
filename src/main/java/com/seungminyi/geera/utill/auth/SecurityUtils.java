package com.seungminyi.geera.utill.auth;

import org.springframework.security.core.context.SecurityContextHolder;

import com.seungminyi.geera.auth.dto.CustomUserDetails;

public class SecurityUtils {
    public static CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
