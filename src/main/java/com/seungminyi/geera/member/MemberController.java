package com.seungminyi.geera.member;

import static com.seungminyi.geera.utill.validator.ValidationUtil.handleBindingErrors;

import java.util.Collections;
import java.util.Optional;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.utill.session.SessionManager;

@RestController
@RequestMapping("/members")
public class MemberController {
    private static final int RANDOM_NUMBER_LENGTH = 6;
    private final MemberService memberService;
    private final SessionManager sessionManager;

    public MemberController(MemberService memberService, SessionManager sessionManager) {
        this.memberService = memberService;
        this.sessionManager = sessionManager;
    }

    @PostMapping()
    public ResponseEntity<?> register(@Validated @RequestBody MemberRequest memberRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleBindingErrors(bindingResult);
        }

        if (!securityCodeCheck(memberRequest.getEmail(), memberRequest.getSecurityCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증코드가 일치하지 않습니다.");
        }

        Member member = memberRequest.toMember();

        try {
            memberService.registerMember(member);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof JdbcSQLIntegrityConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일 입니다.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 가입 중 오류가 발생했습니다.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입이 완료 되었습니다");
    }

    @PostMapping(value = "/verify-email")
    public ResponseEntity<?> verifyEmail(@Validated @RequestBody EmailRequest emailRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleBindingErrors(bindingResult);
        }
        sessionManager.setAttribute(emailRequest.getEmailAddress(), generateRandomNumber());
        return ResponseEntity.ok("이메일 인증코드를 발송했습니다.");
    }

    @GetMapping("/members/email/{emailAddress}")
    public ResponseEntity<?> findMember(@PathVariable String emailAddress) {
        Optional<Member> member = Optional.ofNullable(memberService.findMemberByEmail(emailAddress));
        return ResponseEntity.ok(member);
    }

    private boolean securityCodeCheck(String email, String securityCode) {
        String storedSecurityCode = (String)sessionManager.getAttribute(email);
        return storedSecurityCode != null && storedSecurityCode.equals(securityCode);
    }

    private String generateRandomNumber() {
        int min = (int)Math.pow(10, RANDOM_NUMBER_LENGTH - 1);
        int max = (int)Math.pow(10, RANDOM_NUMBER_LENGTH) - 1;

        return String.format("%06d", (int)(Math.random() * (max - min + 1)) + min);
    }
}
