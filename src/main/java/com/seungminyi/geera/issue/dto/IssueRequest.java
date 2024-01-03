package com.seungminyi.geera.issue.dto;

import java.util.Date;
import java.util.List;

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
    private Integer issuePriority;
    private Long topIssue;
    private List<IssueAssignee> assignees;

    public IssueRequest() {
        this.issuePriority = 3;
    }
    public Issue toIssue() {
        return new Issue()
            .setProjectId(projectId)
            .setIssueType(issueType)
            .setIssueStatus(issueStatus)
            .setIssueDescription(issueDescription)
            .setIssueDetail(issueDetail)
            .setIssuePriority(issuePriority)
            .setTopIssue(topIssue)
            .setAssignees(assignees);
    }
}
