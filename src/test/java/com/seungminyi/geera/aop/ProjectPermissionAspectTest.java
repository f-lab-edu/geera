package com.seungminyi.geera.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRole;
import com.seungminyi.geera.utill.annotation.IssuePermissionCheck;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;

@SpringBootTest
class ProjectPermissionAspectTest {
    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private ProjectPermissionAspect projectPermissionAspect;

    private final ProjectMemberRole[] PROJECT_MEMBER_ROLE_TYPES = {ProjectMemberRole.CREATOR, ProjectMemberRole.MEMBER};
    @BeforeEach
    private void setUp() {
        TestUtil.setAuthentication(
            TestUtil.createCustomUserDetails(
                TestUtil.createTestMember()
            )
        );
    }
    @Test
    @DisplayName("AOP 프로젝트 권한 확인")
    public void testCheckProjectPermissionAspect() {
        ProjectPermissionCheck annotation = mock(ProjectPermissionCheck.class);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRole.CREATOR);

        projectPermissionAspect.checkProjectPermission(null, annotation, 1L);
    }

    @Test
    @DisplayName("AOP 프로젝트 권한 없음")
    public void testCheckProjectPermissionAspect_권한없음() {
        ProjectPermissionCheck annotation = mock(ProjectPermissionCheck.class);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(null);

        assertThrows(InsufficientPermissionException.class, () ->
            projectPermissionAspect.checkProjectPermission(null, annotation, 1L)
        );
    }

    @Test
    @DisplayName("AOP 이슈 권한 확인")
    public void testCheckIssuePermissionAspect() {
        IssuePermissionCheck annotation = mock(IssuePermissionCheck.class);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRole.CREATOR);

        projectPermissionAspect.checkIssuePermission(null, annotation, 1L);
    }

    @Test
    @DisplayName("AOP 이슈 권한 없음")
    public void testCheckIssuePermissionAspect_권한없음() {
        IssuePermissionCheck annotation = mock(IssuePermissionCheck.class);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRole.INVITED);

        assertThrows(InsufficientPermissionException.class, () ->
            projectPermissionAspect.checkIssuePermission(null, annotation, 1L)
        );
    }
}