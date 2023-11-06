package com.seungminyi.geera.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

class CustomAuthenticationSuccessHandlerTest {

    @Test
    @DisplayName("인증 성공 핸들러")
    public void testOnAuthenticationSuccess() throws IOException, ServletException {
        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        Authentication authentication = mock(Authentication.class);

        successHandler.onAuthenticationSuccess(request, response, filterChain, authentication);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
        assertEquals("로그인 성공", response.getContentAsString());
    }
}