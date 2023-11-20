package com.seungminyi.geera.member;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.seungminyi.geera.member.dto.Member;

@SpringBootTest
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    public void registerMemberTest() {
        String testEmail = "test@example.com";
        String testPassword = "password1!";
        String testName = "Test User";
        Member member = new Member();
        member.setEmail(testEmail)
            .setPassword(testPassword)
            .setName(testName);

        when(passwordEncoder.encode(member.getPassword())).thenReturn("encodedPassword");
        memberService.registerMember(member);

        verify(passwordEncoder).encode("password1!");
        verify(memberRepository).insert(member);
    }

    @Test
    public void testFindMemberById() {
        Member member = new Member();
        member.setEmail("test@example.com");
        member.setPassword("password1!");
        member.setName("Test User");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(member);
        Member findMember = memberService.findMemberByEmail(member.getEmail());

        assertEquals(member, findMember);
        verify(memberRepository).findByEmail(member.getEmail());
    }
}