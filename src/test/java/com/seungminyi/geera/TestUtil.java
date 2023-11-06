package com.seungminyi.geera;

import static org.mockito.Mockito.*;

import com.seungminyi.geera.member.dto.Member;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.project.dto.Project;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public static List<Project> createTestProjects() {
        List<Project> projects = new ArrayList<>();

        Project project1 = new Project();
        project1.setProjectId(1L);
        project1.setProjectName("Project 1");
        project1.setCreateAt(new Date());

        Project project2 = new Project();
        project2.setProjectId(2L);
        project2.setProjectName("Project 2");
        project2.setCreateAt(new Date());

        projects.add(project1);
        projects.add(project2);

        return projects;
    }

    public static Project createTestProject() {
        Project project = new Project();
        project.setProjectId(1L);
        project.setProjectName("Test Project");
        project.setCreateAt(new Date());

        return project;
    }

    public static CustomUserDetails createCustomUserDetails(Member member) {
        return new CustomUserDetails(createTestMember());
    }

    public static void setAuthentication(CustomUserDetails customUserDetails) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(
            new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities())
        );
        SecurityContextHolder.setContext(securityContext);
    }
}