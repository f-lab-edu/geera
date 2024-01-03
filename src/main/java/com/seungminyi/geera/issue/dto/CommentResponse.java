package com.seungminyi.geera.issue.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private Long IssueId;
    private String commentContent;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
