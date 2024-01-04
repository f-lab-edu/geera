package com.seungminyi.geera.issue.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IssueAssignee {
    private Long issueAssigneeId;
    private Long issueId;
    private Long memberId;
}
