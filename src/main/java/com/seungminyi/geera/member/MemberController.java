package com.seungminyi.geera.member;

import com.seungminyi.geera.utill.session.SessionManager;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
public class MemberController {

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
            return handleValidationErrors(bindingResult);
        }

        if (!securityCodeCheck(memberRequest.getSecurityCode(), memberRequest.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Member member = memberRequestToMember(memberRequest);
        Member selectMember = memberService.findMemberById(member.getId());

        if (selectMember != null) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일 입니다."); }

        Member registeredMember = memberService.registerMember(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredMember);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Email @RequestBody Map<String, String> requestBody) {
        String emailAddress = requestBody.get("email_address");
        String randomSecurityCode = generateRandomNumber();

        while (sessionManager.getAttribute(randomSecurityCode) != null) {
            randomSecurityCode = generateRandomNumber();
        }

        sessionManager.setAttribute(randomSecurityCode, emailAddress);

        // TODO 6자리 코드 메일 전송

        return ResponseEntity.ok("이메일 인증코드를 발송했습니다.");
    }

    private ResponseEntity<?> handleValidationErrors(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorMessages);
    }

    private boolean securityCodeCheck(String securityCode, String email) {
        String storedEmail = (String) sessionManager.getAttribute(securityCode);
        return storedEmail != null && storedEmail.equals(email);
    }

    private Member memberRequestToMember(MemberRequest memberRequest) {
        Member member = new Member();
        member.setId(memberRequest.getId());
        member.setPassword(memberRequest.getPassword());
        member.setName(memberRequest.getName());
        return member;
    }

    private String generateRandomNumber() {
        final int RANDOM_NUMBER_LENGTH = 6;

        int min = (int) Math.pow(10, RANDOM_NUMBER_LENGTH - 1);
        int max = (int) Math.pow(10, RANDOM_NUMBER_LENGTH) - 1;

        return String.format("%06d", (int) (Math.random() * (max - min + 1)) + min);
    }
}
