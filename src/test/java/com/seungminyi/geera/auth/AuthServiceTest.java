package com.seungminyi.geera.auth;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.auth.AuthService;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.auth.CustomUserDetailsService;
import com.seungminyi.geera.auth.JwtTokenProvider;
import com.seungminyi.geera.auth.dto.LoginResponse;
import com.seungminyi.geera.member.dto.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인 서비스 정상")
    void login() {
        Member member = TestUtil.createTestMember();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Mockito.when(userDetailsService.loadUserByUsername(member.getEmail())).thenReturn(customUserDetails);
        Mockito.when(passwordEncoder.matches(member.getPassword(), member.getPassword())).thenReturn(Boolean.TRUE);
        Mockito.when(jwtTokenProvider.generateToken(customUserDetails)).thenReturn("jwt token");

        LoginResponse response = authService.login(member.getEmail(), member.getPassword());

        assertNotNull(response);
    }

    @Test
    @DisplayName("로그인 서비스 실패 - 패스워드 불일치")
    void loginFailure_패스워드_불일치() {
        Member member = TestUtil.createTestMember();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Mockito.when(userDetailsService.loadUserByUsername(member.getEmail())).thenReturn(customUserDetails);
        Mockito.when(passwordEncoder.matches(member.getPassword(), member.getPassword())).thenReturn(Boolean.FALSE);

        assertThrows(UsernameNotFoundException.class,
                () -> authService.login(member.getEmail(), member.getPassword()));
    }

    @Test
    @DisplayName("로그인 서비스 실패 - 없는유저")
    void loginFailure_없는유저() {
        Member member = TestUtil.createTestMember();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Mockito.when(userDetailsService.loadUserByUsername(member.getEmail())).thenReturn(null);
        Mockito.when(passwordEncoder.matches(member.getPassword(), member.getPassword())).thenReturn(Boolean.TRUE);

        assertThrows(UsernameNotFoundException.class,
                () -> authService.login(member.getEmail(), member.getPassword()));
    }
}