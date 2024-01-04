package com.seungminyi.geera.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.seungminyi.geera.auth.AuthService;
import com.seungminyi.geera.auth.dto.LoginResponse;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Mockito.reset(authService);
    }

    @Test
    @DisplayName("로그인 성공")
    void authenticateUseSuccess() throws Exception {
        Long testMemberId = 1L;
        String testEmail = "test@example.com";
        String testPassword = "password1!";
        String testUserName = "test";
        String testToken = "testToken";
        LoginResponse loginResponse = new LoginResponse()
            .setMemberId(testMemberId)
            .setEmail(testEmail)
            .setUsername(testUserName)
            .setToken(testToken);

        Mockito.when(authService.login(testEmail, testPassword)).thenReturn(loginResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .content("{\n" +
                    "    \"email\" : \"" + testEmail + "\",\n" +
                    "    \"password\" : \"" + testPassword + "\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(testToken))
            .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(testMemberId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUserName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testEmail));
    }

    @Test
    @DisplayName("로그인 실패")
    void authenticateUseFailure() throws Exception {
        String testEmail = "test@example.com";
        String testPassword = "password1!";

        Mockito.when(authService.login(testEmail, testPassword))
            .thenThrow(new UsernameNotFoundException("아이디 혹은 패스워드가 일치하지 않습니다."));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .content("{\n" +
                    "    \"email\" : \"" + testEmail + "\",\n" +
                    "    \"password\" : \"" + testPassword + "\"\n" +
                    "}")
                .contentType("application/json"))

            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}