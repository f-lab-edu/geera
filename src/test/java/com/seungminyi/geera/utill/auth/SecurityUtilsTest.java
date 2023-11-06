package com.seungminyi.geera.utill.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.auth.dto.CustomUserDetails;

class SecurityUtilsTest {
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        securityUtils = new SecurityUtils();
    }

    @Test
    @DisplayName("현재 유저정보")
    public void testGetCurrentUser() {
        TestUtil.setAuthentication(
            TestUtil.createCustomUserDetails(
                TestUtil.createTestMember()
            )
        );
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();
        assertEquals("Test User", currentUser.getUsername());
    }
}