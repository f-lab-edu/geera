package com.seungminyi.geera.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        member.setEmail(testEmail);
        member.setPassword(testPassword);
        member.setName(testName);

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

        when(memberRepository.findMemberById(member.getEmail())).thenReturn(member);
        Member findMember = memberService.findMemberById(member.getEmail());

        assertEquals(member, findMember);
        verify(memberRepository).findMemberById(member.getEmail());
    }
}