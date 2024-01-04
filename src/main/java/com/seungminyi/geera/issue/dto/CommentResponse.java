package com.seungminyi.geera.issue.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private Long issueId;
    private String commentContent;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
