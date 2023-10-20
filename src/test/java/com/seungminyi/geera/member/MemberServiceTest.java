package com.seungminyi.geera.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        memberRepository = mock(MemberRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    public void registerMemberTest() {
        Member member = new Member();
        member.setId("test@example.com");
        member.setPassword("password1!");
        member.setName("Test User");
        when(passwordEncoder.encode(member.getPassword())).thenReturn("encodedPassword");

        memberService.registerMember(member);

        verify(passwordEncoder).encode("password1!");
        verify(memberRepository).insert(member);
    }

    @Test
    public void testFindMemberById() {
        Member member = new Member();
        member.setId("test@example.com");
        member.setPassword("password1!");
        member.setName("Test User");

        when(memberRepository.findMemberById(member.getId())).thenReturn(member);
        Member findMember = memberService.findMemberById(member.getId());

        assertEquals(member, findMember);
        verify(memberRepository).findMemberById(member.getId());
    }
}