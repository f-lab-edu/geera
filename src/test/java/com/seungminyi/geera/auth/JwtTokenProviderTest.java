package com.seungminyi.geera.auth;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.auth.JwtTokenProvider;
import com.seungminyi.geera.member.dto.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateTokenTest() {
        Member testMember = TestUtil.createTestMember();
        CustomUserDetails userDetails = new CustomUserDetails(testMember);

        String token = jwtTokenProvider.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void validateTokenSuccess() {
        String token = TestUtil.generateToken();
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("토큰 검증 실패")
    void validateTokenFailure() {
        String wrongToken = "wrongToken";
        assertFalse(jwtTokenProvider.validateToken(wrongToken));
    }

    @Test
    @DisplayName("Authorization 토큰 파싱")
    public void testResolveTokenWithBearerToken() {
        String bearerToken = "Bearer Token";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals("Token", token);
    }

    @Test
    @DisplayName("Authorization 토큰 없음")
    public void testResolveTokenWithEmptyAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals(null, token);
    }

    @Test
    @DisplayName("Authorization Bearer 아님")
    public void testResolveTokenWithNoBearerToken() {
        when(request.getHeader("Authorization")).thenReturn("Basic Token");

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals(null, token);
    }
}