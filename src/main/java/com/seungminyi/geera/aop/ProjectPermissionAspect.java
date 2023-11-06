package com.seungminyi.geera.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRoleType;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

@Component
@Aspect
public class ProjectPermissionAspect {

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectPermissionAspect(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    @Before("@annotation(projectPermissionCheck) && args(projectId, ..)")
    public void checkProjectPermission(JoinPoint joinPoint,
        ProjectPermissionCheck projectPermissionCheck,
        Long projectId) {
        String errorMessage = "프로젝트 " + projectPermissionCheck.value() + "만 요청할 수 있습니다.";
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
        ProjectMember projectMember = ProjectMember.builder()
            .memberId(userDetails.getId())
            .projectId(projectId)
            .role(projectPermissionCheck.value())
            .build();
        ProjectMemberRoleType roleByMember = projectMemberRepository.findRoleByMember(projectMember);
        if (roleByMember == null) {
            throw new InsufficientPermissionException(errorMessage);
        }
    }
}
