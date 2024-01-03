package com.seungminyi.geera.issue;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.seungminyi.geera.issue.dto.Comment;
import com.seungminyi.geera.issue.dto.CommentRequest;
import com.seungminyi.geera.issue.dto.CommentResponse;
import com.seungminyi.geera.utill.annotation.IssuePermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IssueCommentService {

    private final IssueCommentRepository issueCommentRepository;

    @IssuePermissionCheck
    public List<CommentResponse> getCommentsByIssueId(Long issueId) {
        return issueCommentRepository.selectAllByIssueId(issueId);
    }

    @IssuePermissionCheck
    public void createCommentForIssue(Long issueId, CommentRequest commentRequest) {
        Comment comment = new Comment()
            .setMemberId(SecurityUtils.getCurrentUser().getId())
            .setIssueId(issueId)
            .setContent(commentRequest.getCommentContent())
            .setCreateAt(LocalDateTime.now());
        issueCommentRepository.insert(comment);
    }

    public void updateComment(Long commentId, CommentRequest commentRequest) { //TODO 본인이 작성한 덧글 확인, 권한
        Comment comment = new Comment()
            .setIssueCommentId(commentId)
            .setUpdateAt(LocalDateTime.now())
            .setContent(commentRequest.getCommentContent());
        issueCommentRepository.update(comment);
    }

    public void deleteComment(Long commentId) {
        issueCommentRepository.delete(commentId);
    } //TODO 본인이 작성한 덧글 확인, 권한
}
