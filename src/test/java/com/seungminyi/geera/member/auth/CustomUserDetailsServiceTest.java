package com.seungminyi.geera.member.auth;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.auth.CustomUserDetailsService;
import com.seungminyi.geera.member.dto.Member;
import com.seungminyi.geera.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("UserDetails 서비스 성공")
    void loadUserByUsernameSuccess() {
        Member member = TestUtil.createTestMember();

        Mockito.when(memberRepository.findByEmail(member.getEmail())).thenReturn(member);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(member.getEmail());

        assertEquals(userDetails, new CustomUserDetails(member));
    }
    @Test
    @DisplayName("UserDetails 서비스 실패")
    void loadUserByUsernameFailure() {
        Member member = TestUtil.createTestMember();

        Mockito.when(memberRepository.findByEmail(member.getEmail())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(member.getEmail()));
    }
}