package com.seungminyi.geera.member.auth;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.auth.JwtTokenFilter;
import com.seungminyi.geera.auth.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.verify;

@SpringBootTest
class JwtTokenFilterTest {
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    JwtTokenFilter jwtTokenFilter;

    @Test
    @DisplayName("Jwt 토큰필터 성공")
    void doFilterInternalSuccess() throws ServletException, IOException {
        String token = TestUtil.generateToken();
        Mockito.when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(Boolean.TRUE);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Jwt필터 실패 - 유효하지 않은 토큰")
    void doFilterInternalFailure() throws ServletException, IOException {
        String wrongToken = "Wrong Token";
        PrintWriter printWriter = new PrintWriter(new StringWriter());

        Mockito.when(jwtTokenProvider.resolveToken(request)).thenReturn(wrongToken);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(jwtTokenProvider.validateToken(wrongToken)).thenReturn(Boolean.FALSE);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}