package com.seungminyi.geera.issue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.exception.MaxItemsExceededException;
import com.seungminyi.geera.exception.UnauthorizedException;
import com.seungminyi.geera.issue.dto.Issue;
import com.seungminyi.geera.issue.dto.IssueConditionsDto;
import com.seungminyi.geera.issue.dto.IssueRequest;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRole;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.utill.auth.SecurityUtils;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

	@Mock
	private IssueRepository issueRepository;
	@Mock
	private ProjectMemberRepository projectMemberRepository;

	private IssueService issueService;

	@BeforeEach
	public void setUp() {
		Mockito.reset(issueRepository, projectMemberRepository);
		issueService = new IssueService(issueRepository, projectMemberRepository);

		TestUtil.setAuthentication(
			TestUtil.createCustomUserDetails(
				TestUtil.createTestMember()
			)
		);
	}
	@Test
	@DisplayName("이슈 생성")
	void createIssue() {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		ProjectMember projectMember = new ProjectMember()
			.setMemberId(SecurityUtils.getCurrentUser().getId())
			.setProjectId(issueRequest.getProjectId());

		when(projectMemberRepository.findRoleByMember(projectMember)).thenReturn(ProjectMemberRole.MEMBER);

		issueService.createIssue(issueRequest);

		verify(issueRepository).create(any(Issue.class));
	}

	@Test
	@DisplayName("이슈 생성 실패 - 프로젝트 맴버아님")
	void createIssueFailure() {
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		ProjectMember projectMember = new ProjectMember()
			.setMemberId(SecurityUtils.getCurrentUser().getId())
			.setProjectId(issueRequest.getProjectId());

		when(projectMemberRepository.findRoleByMember(projectMember)).thenReturn(ProjectMemberRole.INVITED);

		assertThrows(UnauthorizedException.class, () ->
			issueService.createIssue(issueRequest));
	}


	@Test
	@DisplayName("이슈 조회")
	public void GetIssuesWithConditions() throws IOException {
		Long project = 1L;
		int page = 1;
		int limit = 49;
		String sort = "issueId";
		String order = "asc";
		String query = "issue_id = 2";

		List<Issue> mockIssues = new ArrayList<>();
		mockIssues.add(TestUtil.createIssueRequest().toIssue());
		when(issueRepository.getWithConditions(any(IssueConditionsDto.class), any(RowBounds.class)))
			.thenReturn(mockIssues);

		List<Issue> issues = issueService.getIssuesWithConditions(project, page, limit, sort, order, query);

		verify(issueRepository).getWithConditions(any(IssueConditionsDto.class), any(RowBounds.class));
		assertEquals(issues, mockIssues);
	}

	@Test
	@DisplayName("이슈 조회 실패 - limit 최대 허용범위 초과")
	public void GetIssuesWithConditions_Failure() {
		Long project = 1L;
		int page = 1;
		int limit = 51;
		String sort = "issueId";
		String order = "asc";
		String query = "";

		assertThrows(MaxItemsExceededException.class, () ->
			issueService.getIssuesWithConditions(project, page, limit, sort, order, query));
	}

	@Test
	@DisplayName("이슈 삭제")
	void deleteIssue() {
		Long issueId = 1L;

		issueService.deleteIssue(issueId);

		verify(issueRepository).updateSubIssuesOnParentDeletion(issueId);
		verify(issueRepository).delete(issueId);
	}

	@Test
	@DisplayName("이슈 수정")
	void updateIssue() {
		Long issueId = 1L;
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		ProjectMember projectMember = new ProjectMember()
			.setMemberId(SecurityUtils.getCurrentUser().getId())
			.setProjectId(issueRequest.getProjectId());

		when(projectMemberRepository.findRoleByMember(projectMember)).thenReturn(ProjectMemberRole.MEMBER);

		issueService.putIssue(issueId, issueRequest);

		verify(issueRepository).update(any(Issue.class));
		verify(issueRepository).deleteAssignees(issueId);
		verify(issueRepository).insertAssignee(eq(issueId), anyLong());
	}

	@Test
	@DisplayName("이슈 부분 수정")
	void patchIssue() {
		Long issueId = 1L;
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		ProjectMember projectMember = new ProjectMember()
			.setMemberId(SecurityUtils.getCurrentUser().getId())
			.setProjectId(issueRequest.getProjectId());

		when(projectMemberRepository.findRoleByMember(projectMember)).thenReturn(ProjectMemberRole.MEMBER);

		issueService.patchIssue(issueId, issueRequest);

		verify(issueRepository).patch(any(Issue.class));
		verify(issueRepository).deleteAssignees(issueId);
		verify(issueRepository).insertAssignee(eq(issueId), anyLong());
	}

	@Test
	@DisplayName("이슈 수정 실패 - 프로젝트 맴버 아님")
	void updateIssueFailure() {
		Long issueId = 1L;
		IssueRequest issueRequest = TestUtil.createIssueRequest();
		ProjectMember projectMember = new ProjectMember()
			.setMemberId(SecurityUtils.getCurrentUser().getId())
			.setProjectId(issueRequest.getProjectId());

		when(projectMemberRepository.findRoleByMember(projectMember)).thenReturn(ProjectMemberRole.INVITED);

		assertThrows(UnauthorizedException.class, () ->
			issueService.putIssue(issueId, issueRequest));
	}
}