package com.seungminyi.geera.member;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.member.dto.Member;
import com.seungminyi.geera.utill.session.SessionManager;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        Mockito.reset(sessionManager);
    }

    @Test
    public void testVerifyEmailSuccess() throws Exception {
        String emailAddress = "test@example.com";

        mockMvc.perform(MockMvcRequestBuilders.post("/members/verify-email")
                .content("{\n" +
                    "    \"email_address\" : \"" + emailAddress  + "\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        String emailAddress = "test@example.com";
        String securityCode = "123456";

        Mockito.when(sessionManager.getAttribute(emailAddress)).thenReturn(securityCode);

        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .content("{\n" +
                    "    \"email\" : \"test@example.com\",\n" +
                    "    \"password\" : \"password1!\",\n" +
                    "    \"name\" : \"Test User\",\n" +
                    "    \"security_code\" : \"123456\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 바인딩 에러 - name null")
    public void testRegisterFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .content("{\n" +
                    "    \"email\" : \"test@example.com\",\n" +
                    "    \"password\" : \"password1!\",\n" +
                    "    \"security_code\" : \"123456\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void TestRegisterFailure_중복된_이메일() throws Exception {
        Member testMember = TestUtil.createTestMember();
        String securityCode = "123456";
        JdbcSQLIntegrityConstraintViolationException uniqueConstraintViolation = new JdbcSQLIntegrityConstraintViolationException(
            "Unique constraint violation",
            "INSERT INTO MEMBER  VALUES (duplicate_value)",
            "23",
            0,
            null,
            null
        );

        Mockito.when(sessionManager.getAttribute(testMember.getEmail())).thenReturn(securityCode);
        doThrow(new DataIntegrityViolationException("Test exception", uniqueConstraintViolation))
            .when(memberService)
            .registerMember(any(Member.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .content("{\n" +
                    "    \"email\" : \"test@example.com\",\n" +
                    "    \"password\" : \"password1!\",\n" +
                    "    \"name\" : \"Test User\",\n" +
                    "    \"security_code\" : \"123456\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testRegisterFailure_인증코드_불일치() throws Exception {
        String emailAddress = "test@example.com";
        String incorrectSecurityCode = "123456";

        Mockito.when(sessionManager.getAttribute(emailAddress)).thenReturn(incorrectSecurityCode);

        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .content("{\n" +
                    "    \"email\" : \"test@example.com\",\n" +
                    "    \"password\" : \"password1!\",\n" +
                    "    \"name\" : \"Test User\",\n" +
                    "    \"security_code\" : \"654321\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRegisterFailure_인증코드_없음() throws Exception {
        String emailAddress = "test@example.com";

        Mockito.when(sessionManager.getAttribute(emailAddress)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .content("{\n" +
                    "    \"email\" : \"test@example.com\",\n" +
                    "    \"password\" : \"password1!\",\n" +
                    "    \"name\" : \"Test User\",\n" +
                    "    \"security_code\" : \"654321\"\n" +
                    "}")
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}