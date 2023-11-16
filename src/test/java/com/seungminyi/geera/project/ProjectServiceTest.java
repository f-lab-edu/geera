package com.seungminyi.geera.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.project.dto.Project;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.project.dto.ProjectQuery;
import com.seungminyi.geera.project.dto.ProjectRequest;

@SpringBootTest
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        TestUtil.setAuthentication(
            TestUtil.createCustomUserDetails(
                TestUtil.createTestMember()
            )
        );
    }

    @Test
    @DisplayName("프로젝트 생성 서비스")
    void testCreateProject() {
        Project testProject = TestUtil.createTestProject();

        ProjectMember projectMember = new ProjectMember()
            .setProjectId(testProject.getProjectId())
            .setMemberId(getCurrentUser().getId())
            .setRole(ProjectMemberRole.CREATOR);

        Project createdProject = projectService.createProject(testProject);

        assertEquals(testProject, createdProject);
        verify(projectRepository).create(testProject);
        verify(projectMemberRepository).create(projectMember);
    }

    @Test
    @DisplayName("프로젝트 조회 서비스")
    void testGetProjects() {
        List<Project> testProjects = TestUtil.createTestProjects();
        String sortKey = "project_id";
        String sortOrder = "asc";
        int page = 0;
        int size = 10;

        when(projectRepository.findByMember(any(ProjectQuery.class))).thenReturn(testProjects);

        List<Project> projects = projectService.getProjects(sortKey, sortOrder, page, size);

        verify(projectRepository).findByMember(
            Mockito.argThat(query -> query.getStart() == 0
            && query.getSize() == 10
            && query.getMemberId() == getCurrentUser().getId()
            && query.getSortKey().equals(sortKey)
            && query.getSortOrder().equals(sortOrder))
        );
        assertEquals(projects, testProjects);
    }

    @Test
    @DisplayName("프로젝트 업데이트")
    void testUpdateProject() {
        Long projectId = 1L;
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectName("Test Project");

        projectService.updateProject(projectId, projectRequest);

        verify(projectRepository).update(any(Project.class));
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void testDeleteProject() {
        Long projectId = 1L;
        projectService.deleteProject(projectId);

        verify(projectRepository).delete(projectId);
    }

    @Test
    @DisplayName("프로젝트 맴버 초대")
    void testAddProjectMember() {
        Long projectId = 1L;
        Long memberId = 1L;
        projectService.addProjectMember(projectId, memberId);

        verify(projectMemberRepository).create(any(ProjectMember.class));
    }

    @Test
    @DisplayName("프로젝트 멤버 삭제")
    void testDeleteProjectMember() {
        Long projectId = 1L;
        Long memberId = 2L; //TestUtil.setAuthentication() memberId : 1L

        projectService.deleteProjectMember(projectId, memberId);

        verify(projectMemberRepository).delete(any(ProjectMember.class));
    }

    @Test
    @DisplayName("프로젝트 멤버 삭제 - 본인삭제")
    void testDeleteProjectMember_Failure_본인삭제() {
        Long projectId = 1L;
        Long memberId = 1L;

        assertThrows(InsufficientPermissionException.class, () ->
            projectService.deleteProjectMember(projectId, memberId)
        );
    }

    @Test
    @DisplayName("프로젝트 맴버 초대 여부 확인")
    public void testAcceptProjectInvitation() {
        Long projectId = 1L;
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRole.INVITED);

        projectService.acceptProjectInvitation(projectId);

        Mockito.verify(projectMemberRepository).update(any(ProjectMember.class));
    }

    @Test
    @DisplayName("프로젝트 맴버 초대 여부 확인 - 초대받지 않음")
    public void testAcceptProjectInvitation_Failure() {
        Long projectId = 1L;
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(null);

        assertThrows(
            InsufficientPermissionException.class, () ->
            projectService.acceptProjectInvitation(projectId)
        );
    }

    @Test
    @DisplayName("프로젝트 맴버 초대 여부 확인 - Invite 상태가 아님")
    public void testAcceptProjectInvitation_Failure_Not_Invited() {
        Long projectId = 1L;
        when(projectMemberRepository.findRoleByMember(any(ProjectMember.class))).thenReturn(ProjectMemberRole.MEMBER);

        assertThrows(
            InsufficientPermissionException.class, () ->
                projectService.acceptProjectInvitation(projectId)
        );
    }


    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}