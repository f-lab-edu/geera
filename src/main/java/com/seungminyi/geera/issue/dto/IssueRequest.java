package com.seungminyi.geera.issue.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IssueRequest {
    @NotBlank
    private Long projectId;
    @NotBlank
    private IssueType issueType;
    private IssueStatusType issueStatus;
    @NotBlank
    private String issueDescription;
    private String issueDetail;
    private Long issueContractId;
    private Integer issuePriority;
    private Long topIssue;

    public IssueRequest() {
        this.issuePriority = 3;
        this.issueStatus = IssueStatusType.TODO;
    }
    public Issue toIssue() {
        return Issue.builder()
            .projectId(projectId)
            .issueType(issueType)
            .issueStatus(issueStatus)
            .issueDescription(issueDescription)
            .issueDetail(issueDetail)
            .issueContractId(issueContractId)
            .issuePriority(issuePriority)
            .topIssue(topIssue)
            .build();
    }
}
