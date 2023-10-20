package com.seungminyi.geera.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    public void testInsertAndFindMember() {
        Member member = new Member();
        member.setId("test@example.com");
        member.setName("Test User");
        member.setPassword("EncodedPassword");

        memberRepository.insert(member);

        Member foundMember = memberRepository.findMemberById("test@example.com");

        assertEquals("test@example.com", foundMember.getId());
        assertEquals("Test User", foundMember.getName());
    }

    @Test
    @DisplayName("존재하지 않는 유저 찾기")
    public void testFindNonExistentMember() {
        Member nonExistentMember = memberRepository.findMemberById("nonexistent@example.com");

        assertNull(nonExistentMember);
    }
}