package com.seungminyi.geera.aop;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;

import com.seungminyi.geera.issue.IssueRepository;
import com.seungminyi.geera.issue.dto.IssueRequest;
import com.seungminyi.geera.utill.auth.PermissionRoleGroup;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRole;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.utill.annotation.IssuePermissionCheck;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
@AllArgsConstructor
public class ProjectPermissionAspect {

	private final ProjectMemberRepository projectMemberRepository;
	private final IssueRepository issueRepository;

	@Before("@annotation(projectPermissionCheck) && args(projectId, ..)")
	public void checkProjectPermission(JoinPoint joinPoint, ProjectPermissionCheck projectPermissionCheck,
		Long projectId) {
		if (!hasRequiredRole(PermissionRoleGroup.PROJECT_ACCESS_ROLES, projectId)) {
			throw new InsufficientPermissionException("프로젝트 접근 권한이 없는 사용자 입니다.");
		}
	}

	@Before("@annotation(issuePermissionCheck) && args(issueRequest, ..)")
	public void checkIssuePermission(JoinPoint joinPoint, IssuePermissionCheck issuePermissionCheck, IssueRequest issueRequest) {
		System.out.println("issue request!!");
		if (!hasRequiredRole(PermissionRoleGroup.ISSUE_ACCESS_ROLES, issueRequest.getProjectId())) {
			throw new InsufficientPermissionException("이슈 접근 권한이 없는 사용자 입니다.");
		}
	}

	@Before("@annotation(issuePermissionCheck) && args(issueId, ..)")
	public void checkIssuePermission(JoinPoint joinPoint, IssuePermissionCheck issuePermissionCheck, Long issueId) {
		Long projectId = issueRepository.getProjectId(issueId);
		System.out.println("issue id!!");
		if (!hasRequiredRole(PermissionRoleGroup.ISSUE_ACCESS_ROLES, projectId)) {
			throw new InsufficientPermissionException("이슈 접근 권한이 없는 사용자 입니다.");
		}
	}

	private boolean hasRequiredRole(Set<ProjectMemberRole> projectMemberRoles, Long projectId) {
		CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
		ProjectMemberRole memberRole = projectMemberRepository.findRoleByMember(
			new ProjectMember()
				.setMemberId(userDetails.getId())
				.setProjectId(projectId));

		return memberRole != null && projectMemberRoles.contains(memberRole);
	}
}
