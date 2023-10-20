package com.seungminyi.geera.member;

import com.fasterxml.jackson.databind.node.TextNode;
import com.seungminyi.geera.utill.session.SessionManager;
import jakarta.validation.constraints.Email;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.seungminyi.geera.utill.validator.ValidationUtil.handleBindingErrors;

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

            if (!securityCodeCheck(memberRequest.getId(), memberRequest.getSecurityCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증코드가 일치하지 않습니다.");
            }

            Member member = memberRequest.toMember();

            try {
                memberService.registerMember(member);
            } catch (DataIntegrityViolationException e) {
                if (e.getCause() instanceof JdbcSQLIntegrityConstraintViolationException) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일 입니다.");
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 가입 중 오류가 발생했습니다.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입이 완료 되었습니다");
    }

    @PostMapping(value = "/verify-email")
    public ResponseEntity<?> verifyEmail(@Validated @RequestBody VerifyEmailRequest verifyEmailRequest,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleBindingErrors(bindingResult);
        }
        sessionManager.setAttribute(verifyEmailRequest.getEmailAddress(), generateRandomNumber());
        System.out.println(sessionManager.getAttribute(verifyEmailRequest.getEmailAddress()));
        return ResponseEntity.ok("이메일 인증코드를 발송했습니다.");
    }


    private boolean securityCodeCheck(String email, String securityCode) {
        String storedSecurityCode = (String) sessionManager.getAttribute(email);
        return storedSecurityCode != null && storedSecurityCode.equals(securityCode);
    }

    private String generateRandomNumber() {
        int min = (int) Math.pow(10, RANDOM_NUMBER_LENGTH - 1);
        int max = (int) Math.pow(10, RANDOM_NUMBER_LENGTH) - 1;

        return String.format("%06d", (int) (Math.random() * (max - min + 1)) + min);
    }
}
