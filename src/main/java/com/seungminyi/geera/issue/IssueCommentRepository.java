package com.seungminyi.geera.issue;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seungminyi.geera.issue.dto.Comment;
import com.seungminyi.geera.issue.dto.CommentResponse;

@Mapper
public interface IssueCommentRepository {
    List<CommentResponse> selectAllByIssueId(Long issueId);

    void insert(Comment comment);

    void update(Comment comment);

    void delete(Long commentId);

    Comment selectById(Long commentId);
}
