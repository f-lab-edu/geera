package com.seungminyi.geera.issue;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.ibatis.session.RowBounds;

import com.seungminyi.geera.exception.MaxItemsExceededException;
import com.seungminyi.geera.exception.UnauthorizedAssignmentException;
import com.seungminyi.geera.issue.dto.IssueConditionsDto;
import com.seungminyi.geera.issue.dto.Issue;
import com.seungminyi.geera.issue.dto.IssueRequest;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRole;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.utill.annotation.IssuePermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {
	private final IssueRepository issueRepository;
	private final ProjectMemberRepository projectMemberRepository;

	@IssuePermissionCheck
	public void createIssue(IssueRequest issueRequest) {
		validateAssignmentPermission(issueRequest);
		Issue issue = issueRequest.toIssue();
		issue.setIssueReporterId(SecurityUtils.getCurrentUser().getId());
		issue.setCreateAt(new Date());
		issueRepository.create(issue);
	}

	public List<Issue> getIssuesWithConditions(
		Long project,
		int page,
		int limit,
		String sort,
		String order) {

		if (limit > 50) {
			throw new MaxItemsExceededException();
		}

		IssueConditionsDto issueConditionsDto = IssueConditionsDto.builder()
			.project(project)
			.sort(sort)
			.order(order)
			.member(SecurityUtils.getCurrentUser().getId())
			.build();

		RowBounds rowBounds = new RowBounds((page - 1) * limit, limit);
		return issueRepository.getWithConditions(issueConditionsDto, rowBounds);
	}

	@IssuePermissionCheck
	@Transactional
	public void deleteIssue(Long issueId) {
		issueRepository.updateSubIssuesOnParentDeletion(issueId);
		issueRepository.delete(issueId);
	}

	@IssuePermissionCheck
	public void updateIssue(Long issueId, IssueRequest issueRequest) {
		validateAssignmentPermission(issueRequest);
		Issue issue = issueRequest.toIssue();
		issue.setIssueId(issueId);
		issueRepository.update(issue);
	}

	private void validateAssignmentPermission(IssueRequest issueRequest) {
		Long issueContractId = issueRequest.getIssueContractId();
		if (issueContractId != null) {
			checkMemberHasIssueAccess(issueRequest.getProjectId(), issueContractId);
		}
	}

	private void checkMemberHasIssueAccess(Long projectId, Long issueContractId) {
		ProjectMember projectMember = new ProjectMember()
			.setProjectId(projectId)
			.setMemberId(issueContractId);
		ProjectMemberRole memberRole = projectMemberRepository.findRoleByMember(projectMember);

		if (!memberRole.hasIssueAccess()) {
			throw new UnauthorizedAssignmentException("담당자가 프로젝트에 권한이 없습니다.");
		}
	}
}
