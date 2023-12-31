package com.seungminyi.geera.issue.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class Comment {
    private Long issueCommentId;
    private Long memberId;
    private Long issueId;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
