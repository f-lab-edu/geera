package com.seungminyi.geera.issue.dto;

import lombok.Data;

@Data
public class IssueAssignee {
    private Long issueAssigneeId;
    private Long issueId;
    private Long memberId;
}
