package com.seungminyi.geera.issue;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.issue.dto.CommentRequest;
import com.seungminyi.geera.issue.dto.CommentResponse;
import com.seungminyi.geera.issue.dto.IssueRequest;

@SpringBootTest
@AutoConfigureMockMvc
class IssueControllerTest {

	@MockBean
	private IssueService issueService;
	@MockBean
	private IssueCommentService issueCommentService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		Mockito.reset(issueService, issueCommentService);
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 생성")
	void testCreateIssue() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		mockMvc.perform(post("/issues")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"이슈 생성완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 수정")
	void testUpdateIssue() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		mockMvc.perform(put("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"issue 수정 완료\"}"));
	}


	@Test
	@WithMockUser
	@DisplayName("이슈 수정에러 - 상위이슈가 자신")
	void testUpdateIssue_Failure() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		issueRequest.setTopIssue(1L);
		mockMvc.perform(put("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().json("{\"error\": \"상위 이슈는 자기 자신일 수 없습니다.\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 부분 수정")
	void testPatchIssue() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		mockMvc.perform(patch("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"issue 수정 완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 부분 수정에러 - 상위이슈가 자신")
	void testPatchIssue_Failure() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		issueRequest.setTopIssue(1L);

		mockMvc.perform(patch("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().json("{\"error\": \"상위 이슈는 자기 자신일 수 없습니다.\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 삭제")
	void testDeleteIssue() throws Exception {
		mockMvc.perform(delete("/issues/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"issue 삭제 완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 조회")
	void testGetIssues() throws Exception {
		//TODO Query
		mockMvc.perform(get("/issues")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 댓글 작성")
	void testCreateComment() throws Exception {
		Long issueId = 1L;
		CommentRequest commentRequest = new CommentRequest();
		String jsonRequest = TestUtil.convertToJson(commentRequest);

		mockMvc.perform(post("/issues/" + issueId + "/comments")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("comment 생성완료"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 댓글 조회")
	void testGetComment() throws Exception {
		Long issueId = 1L;
		List<CommentResponse> mockResponses = Arrays.asList(
			new CommentResponse(1L, 1L, issueId, "Test Comment 1", LocalDateTime.now(), LocalDateTime.now()),
			new CommentResponse(2L, 1L, issueId, "Test Comment 2", LocalDateTime.now(), LocalDateTime.now())
		);

		given(issueCommentService.getCommentsByIssueId(issueId)).willReturn(mockResponses);

		mockMvc.perform(get("/issues/" + issueId + "/comments")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].commentId").value(mockResponses.get(0).getCommentId()))
			.andExpect(jsonPath("$[0].commentContent").value("Test Comment 1"))
			.andExpect(jsonPath("$[1].commentId").value(mockResponses.get(1).getCommentId()))
			.andExpect(jsonPath("$[1].commentContent").value("Test Comment 2"));
	}

	@Test
	@WithMockUser
	@DisplayName("댓글 수정")
	void testUpdateComment() throws Exception {
		Long commentId = 1L;
		CommentRequest commentRequest = new CommentRequest();
		String jsonRequest = TestUtil.convertToJson(commentRequest);

		mockMvc.perform(patch("/issues/comments/" + commentId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("comment 수정완료"));
	}

	@Test
	@WithMockUser
	@DisplayName("댓글 삭제")
	void testDeleteComment() throws Exception {
		Long commentId = 1L;

		mockMvc.perform(delete("/issues/comments/" + commentId)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("comment 삭제완료"));
	}
}