package com.seungminyi.geera.member.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


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
        String testEmail = "test@example.com";
        String testPassword = "password1!";
        String testToken = "testToken";

        Mockito.when(authService.login(testEmail, testPassword)).thenReturn(testToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("{\n" +
                                "    \"email\" : \"" + testEmail + "\",\n" +
                                "    \"password\" : \"" + testPassword + "\"\n" +
                                "}")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 실패")
    void authenticateUseFailure() throws Exception {
        String testEmail = "test@example.com";
        String testPassword = "password1!";

        Mockito.when(authService.login(testEmail, testPassword)).thenThrow(new UsernameNotFoundException("아이디 혹은 패스워드가 일치하지 않습니다."));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("{\n" +
                                "    \"email\" : \"" + testEmail + "\",\n" +
                                "    \"password\" : \"" + testPassword + "\"\n" +
                                "}")
                        .contentType("application/json"))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}