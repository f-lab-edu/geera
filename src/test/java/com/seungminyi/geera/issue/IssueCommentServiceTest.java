package com.seungminyi.geera.issue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.exception.UnauthorizedException;
import com.seungminyi.geera.issue.dto.Comment;
import com.seungminyi.geera.issue.dto.CommentRequest;
import com.seungminyi.geera.issue.dto.CommentResponse;
import com.seungminyi.geera.utill.auth.SecurityUtils;

@ExtendWith(MockitoExtension.class)
class IssueCommentServiceTest {
    @Mock
    private IssueCommentRepository issueCommentRepository;
    private IssueCommentService issueCommentService;

    @BeforeEach
    void setUp() {
        Mockito.reset(issueCommentRepository);
        issueCommentService = new IssueCommentService(issueCommentRepository);

        TestUtil.setAuthentication(
            TestUtil.createCustomUserDetails(
                TestUtil.createTestMember()
            )
        );
    }

    @Test
    @DisplayName("댓글 조회")
    public void testGetComments() {
        List<CommentResponse> mockResponses = Arrays.asList(new CommentResponse(), new CommentResponse());
        when(issueCommentRepository.selectAllByIssueId(1L)).thenReturn(mockResponses);

        List<CommentResponse> responses = issueCommentService.getCommentsByIssueId(1L);
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("댓글 조회 - 댓글없음")
    public void testGetComment_NoComments() {
        when(issueCommentRepository.selectAllByIssueId(1L)).thenReturn(Arrays.asList());

        List<CommentResponse> responses = issueCommentService.getCommentsByIssueId(1L);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("댓글 작성")
    public void testCreateComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCommentContent("댓글 내용");

        issueCommentService.createCommentForIssue(1L, commentRequest);

        verify(issueCommentRepository, times(1)).insert(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 - 작성자가 아님")
    public void testUpdateComment_NotOwner() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCommentContent("수정된 댓글 내용");

        when(issueCommentRepository.selectById(1L)).thenReturn(new Comment().setMemberId(999L));

        assertThrows(UnauthorizedException.class, () -> {
            issueCommentService.updateComment(1L, commentRequest);
        });

    }

    @Test
    @DisplayName("댓글 수정")
    public void testUpdateComment() {
        Comment comment = new Comment()
            .setMemberId(SecurityUtils.getCurrentUser().getId())
            .setContent("이전 댓글 내용");
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCommentContent("수정된 댓글 내용");

        when(issueCommentRepository.selectById(1L)).thenReturn(comment);

        issueCommentService.updateComment(1L, commentRequest);

        verify(issueCommentRepository, times(1)).update(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제 - 작성자가 아님")
    public void testDeleteComment_NotOwner() {
        when(issueCommentRepository.selectById(1L)).thenReturn(new Comment().setMemberId(999L));

        assertThrows(UnauthorizedException.class, () -> {
            issueCommentService.deleteComment(1L);
        });
    }

    @Test
    @DisplayName("댓글 삭제")
    public void testDeleteComment() {
        Comment comment = new Comment().setMemberId(SecurityUtils.getCurrentUser().getId());
        when(issueCommentRepository.selectById(1L)).thenReturn(comment);

        issueCommentService.deleteComment(1L);

        verify(issueCommentRepository, times(1)).delete(1L);
    }
}