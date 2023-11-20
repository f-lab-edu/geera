package com.seungminyi.geera.issue.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class Issue {
    private Long issueId;
    private Long projectId;
    private IssueType issueType;
    private IssueStatusType issueStatus;
    private String issueDescription;
    private String issueDetail;
    private Long issueContractId;
    private Long issueReporterId;
    private Integer issuePriority;
    private Long sprintId;
    private Date createAt;
    private Long topIssue;
}
