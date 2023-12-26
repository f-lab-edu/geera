package com.seungminyi.geera.issue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.issue.dto.IssueRequest;

@SpringBootTest
@AutoConfigureMockMvc
class IssueControllerTest {

	@MockBean
	private IssueService issueService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		Mockito.reset(issueService);
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 생성")
	void createIssue() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		mockMvc.perform(post("/issues")
			.content(objectMapper.writeValueAsString(issueRequest))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"이슈 생성완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 수정")
	void updateIssue() throws Exception {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		mockMvc.perform(put("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"issue 수정 완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 수정에러 - 상위이슈가 자신")
	void updateIssue_Failure() throws Exception{
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		issueRequest.setTopIssue(1L);
		mockMvc.perform(put("/issues/1")
				.content(objectMapper.writeValueAsString(issueRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().json("{\"error\": \"상위 이슈는 자기 자신일 수 없습니다.\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 삭제")
	void deleteIssue() throws Exception {
		mockMvc.perform(delete("/issues/1")
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().json("{\"message\": \"issue 삭제 완료\"}"));
	}

	@Test
	@WithMockUser
	@DisplayName("이슈 조회")
	void getIssues() throws Exception {
		//TODO Query
		mockMvc.perform(get("/issues")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}