package com.seungminyi.geera.issue.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Issue {
    private Long issueId;
    private Long projectId;
    private IssueType issueType;
    private IssueStatusType issueStatus;
    private String issueDescription;
    private String issueDetail;
    private Long issueReporterId;
    private Integer issuePriority;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long topIssue;
    private List<IssueAssignee> assignees = new ArrayList<>();
}
