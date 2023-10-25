package com.seungminyi.geera;

import com.seungminyi.geera.member.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestUtil {

    public static Member createTestMember() {
        Long testId = 1L;
        String testEmail = "test@example.com";
        String testPassword = "password1!";
        String testName = "Test User";
        Member member = new Member();
        member.setMemberId(testId);
        member.setEmail(testEmail);
        member.setPassword(testPassword);
        member.setName(testName);
        return member;
    }

    public static String generateToken() {
        String secretKey = "1234";
        int validityInMilliseconds = 36000;
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        Member member = createTestMember();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(member.getName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}