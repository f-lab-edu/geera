package com.seungminyi.geera.member.auth;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
}