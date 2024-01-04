package com.seungminyi.geera.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.auth.dto.LoginResponse;

@Service
public class AuthService {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        CustomUserDetailsService userDetailsService,
        JwtTokenProvider jwtTokenProvider,
        PasswordEncoder passwordEncoder
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(String id, String password) {
        CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(id);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UsernameNotFoundException("아이디 혹은 패스워드가 일치하지 않습니다.");
        }
        return new LoginResponse()
            .setMemberId(userDetails.getId())
            .setToken(jwtTokenProvider.generateToken(userDetails))
            .setUsername(userDetails.getUsername())
            .setEmail(userDetails.getEmail());
    }
}
