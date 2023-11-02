package com.seungminyi.geera.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.seungminyi.geera.exception.ProjectPermissionException;
import com.seungminyi.geera.member.auth.CustomUserDetails;
import com.seungminyi.geera.project.ProjectMember;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRoleType;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;

@Component
@Aspect
public class ProjectPermissionAspect {

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectPermissionAspect(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    @Before("@annotation(projectPermissionCheck) && args(projectId, ..)")
    public void checkProjectPermission(JoinPoint joinPoint, ProjectPermissionCheck projectPermissionCheck, Long projectId) {
        String errorMessage = "프로젝트 " + projectPermissionCheck.value() + "만 요청할 수 있습니다.";
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectMember projectMember = new ProjectMember();
        projectMember.setMemberId(userDetails.getId());
        projectMember.setProjectId(projectId);
        projectMember.setRole(projectPermissionCheck.value());
        ProjectMemberRoleType roleByMember = projectMemberRepository.findRoleByMember(projectMember);
        if (roleByMember == null) {
            throw new ProjectPermissionException(errorMessage);
        }
    }
}
