package com.seungminyi.geera.issue;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.common.dto.ErrorResponseMessage;
import com.seungminyi.geera.common.dto.ResponseMessage;
import com.seungminyi.geera.issue.dto.Issue;
import com.seungminyi.geera.issue.dto.IssueRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@SecurityRequirement(name = "Authorization")
@Tag(name = "이슈")
@RestController
@RequestMapping("/issues")
@Slf4j
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @Operation(summary = "이슈 생성")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @PostMapping
    public ResponseEntity<?> createIssue(@RequestBody IssueRequest issueRequest) {
        issueService.createIssue(issueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("이슈 생성완료"));
    }

    @Operation(summary = "이슈 조회")
    @ApiResponse(responseCode = "200", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Issue.class)), mediaType = "application/json")})
    @GetMapping
    public ResponseEntity<?> getIssues(
            @RequestParam(required = false) Long project,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "") String query
    ) throws IOException {
        List<Issue> issuesWithConditions = issueService.getIssuesWithConditions(project,
                page, limit, sort, order, query);
        return ResponseEntity.ok(issuesWithConditions);
    }

    @Operation(summary = "이슈 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", content = {
            @Content(schema = @Schema(implementation = ErrorResponseMessage.class), mediaType = "application/json")})
    })
    @PutMapping("/{issueId}")
    public ResponseEntity<?> updateIssue(@PathVariable Long issueId,
                                         @RequestBody IssueRequest issueRequest) {
        if (issueId.equals(issueRequest.getTopIssue())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseMessage("상위 이슈는 자기 자신일 수 없습니다."));
        }
        issueService.updateIssue(issueId, issueRequest);
        return ResponseEntity.ok(new ResponseMessage("issue 수정 완료"));
    }

    @Operation(summary = "이슈 삭제")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @DeleteMapping("/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ResponseEntity.ok(new ResponseMessage("issue 수정 완료"));
    }
}
