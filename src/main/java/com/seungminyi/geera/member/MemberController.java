package com.seungminyi.geera.member;

import static com.seungminyi.geera.utill.validator.ValidationUtil.handleBindingErrors;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

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

import com.seungminyi.geera.common.dto.ResponseMessage;
import com.seungminyi.geera.member.dto.EmailRequest;
import com.seungminyi.geera.member.dto.Member;
import com.seungminyi.geera.member.dto.MemberRequest;
import com.seungminyi.geera.member.dto.ProjectInfo;
import com.seungminyi.geera.member.dto.VerifyEmailResponse;
import com.seungminyi.geera.utill.session.SessionManager;
import com.seungminyi.geera.utill.validator.ValidationUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "맴버", description = "맴버 관련 API")
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

    @Operation(summary = "회원 가입", description = "맴버를 등록합니다")
    @Parameters({
        @Parameter(name = "email", description = "email 주소"),
        @Parameter(name = "password", description = "패스워드 8자리 이상 영문자 특수문자 조합"),
        @Parameter(name = "name", description = "사용자명"),
        @Parameter(name = "security_code", description = "이메일 인증코드")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "가입완료", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "인증코드 불일치", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "409", description = "중복된 아이디", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    })
    @PostMapping()
    public ResponseEntity<?> register(@Validated @RequestBody MemberRequest memberRequest,
        BindingResult bindingResult) {
        ValidationUtil.handleBindingErrors(bindingResult);
        if (!securityCodeCheck(memberRequest.getEmail(), memberRequest.getSecurityCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("인증코드가 일치하지 않습니다."));
        }

        Member member = memberRequest.toMember();

        try {
            memberService.registerMember(member);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("이미 가입된 이메일 입니다."));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage("회원 가입 중 오류가 발생했습니다."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("회원 가입이 완료 되었습니다"));
    }

    @Operation(summary = "이메일 인증코드 발송(메일서버 부재로 인증코드를 반환합니다.)", description = "회원가입 선행작업")
    @Parameter(name = "email_address", description = "email 주소")
    @ApiResponse(responseCode = "200", description = "인증코드 발송 완료", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @PostMapping(value = "/verify-email")
    public ResponseEntity<?> verifyEmail(@Validated @RequestBody EmailRequest emailRequest,
        BindingResult bindingResult) {
        ValidationUtil.handleBindingErrors(bindingResult);
        String verificationCode = generateRandomNumber();
        sessionManager.setAttribute(emailRequest.getEmailAddress(), verificationCode);

        //메일서버 부재로 인증코드 반환
        VerifyEmailResponse response = new VerifyEmailResponse(verificationCode);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "맴버 찾기", description = "email로 member 검색")
    @GetMapping("/email")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = Member.class), mediaType = "application/json")})
    public ResponseEntity<?> findMember(@RequestParam String emailAddress) {
        Optional<Member> member = Optional.ofNullable(memberService.findMemberByEmail(emailAddress));
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{memberId}/invited-projects")
    public ResponseEntity<?> getInvitedProjects(@PathVariable Long memberId) {
        List<ProjectInfo> invitedProjects = memberService.getInvitedProjects(memberId);
        return ResponseEntity.ok(invitedProjects);
    }

    private boolean securityCodeCheck(String email, String securityCode) {
        String storedSecurityCode = (String)sessionManager.getAttribute(email);
        return storedSecurityCode != null && storedSecurityCode.equals(securityCode);
    }

    private String generateRandomNumber() {
        int min = (int)Math.pow(10, (RANDOM_NUMBER_LENGTH - 1));
        int max = (int)Math.pow(10, RANDOM_NUMBER_LENGTH) - 1;

        return String.format("%06d", (int)(Math.random() * (max - min + 1)) + min);
    }
}
