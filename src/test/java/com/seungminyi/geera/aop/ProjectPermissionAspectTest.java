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
import com.seungminyi.geera.project.ProjectMemberRoleType;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;

@SpringBootTest
class ProjectPermissionAspectTest {
    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private ProjectPermissionAspect projectPermissionAspect;

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
        when(annotation.value()).thenReturn(ProjectMemberRoleType.CREATOR);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRoleType.CREATOR);

        projectPermissionAspect.checkProjectPermission(null, annotation, 1L);
    }

    @Test
    @DisplayName("AOP 프로젝트 권한 없음")
    public void testCheckProjectPermissionAspect_권한없음() {
        ProjectPermissionCheck annotation = mock(ProjectPermissionCheck.class);
        when(annotation.value()).thenReturn(ProjectMemberRoleType.CREATOR);
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(null);

        assertThrows(InsufficientPermissionException.class, () ->
            projectPermissionAspect.checkProjectPermission(null, annotation, 1L)
        );
    }
}