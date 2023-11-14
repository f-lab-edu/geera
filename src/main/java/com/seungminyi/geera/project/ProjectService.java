package com.seungminyi.geera.project;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.project.dto.Project;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.project.dto.ProjectQuery;
import com.seungminyi.geera.project.dto.ProjectRequest;
import com.seungminyi.geera.utill.annotation.ProjectPermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Transactional
    public Project createProject(Project project) {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();
        project.setCreateAt(new Date());
        projectRepository.create(project);

        ProjectMember projectMember = ProjectMember.builder()
                .projectId(project.getProjectId())
                .memberId(userDetails.getId())
                .role(ProjectMemberRole.CREATOR)
                .build();

        projectMemberRepository.create(projectMember);

        return project;
    }

    public List<Project> getProjects(String sortKey, String sortOrder, int page, int size) {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();

        if (page < 1) {
            page = 1;
            //-1 throw exception
        }
        int start = (page - 1) * size;

        ProjectQuery query = ProjectQuery.builder()
                .memberId(userDetails.getId())
                .sortKey(sortKey)
                .sortOrder(sortOrder)
                .start(start)
                .size(size)
                .build();
        return projectRepository.findByMember(query);
    }

    @ProjectPermissionCheck
    public void updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRequest.toProject();
        project.setProjectId(projectId);
        projectRepository.update(project);
    }

    @ProjectPermissionCheck
    public void deleteProject(Long projectId) {
        projectRepository.delete(projectId);
    }

    @ProjectPermissionCheck
    public void addProjectMember(Long projectId, Long memberId) {
        ProjectMember projectMember = buildProjectMember(projectId, memberId, ProjectMemberRole.INVITED);
        projectMemberRepository.create(projectMember);
    }

    @ProjectPermissionCheck
    public int deleteProjectMember(Long projectId, Long memberId) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();
        if (Objects.equals(currentUser.getId(), memberId)) {
            throw new InsufficientPermissionException("본인을 프로젝트에서 제외할 수 없습니다.");
        }

        ProjectMember projectMember = buildProjectMember(projectId, memberId, ProjectMemberRole.MEMBER);

        return projectMemberRepository.delete(projectMember);
    }

    public void acceptProjectInvitation(Long projectId) {
        checkProjectPermission(projectId, ProjectMemberRole.INVITED, "초대받지 않은 유저 입니다.");
        Long memberId = SecurityUtils.getCurrentUser().getId();
        ProjectMember projectMember =
                buildProjectMember(projectId, memberId, ProjectMemberRole.MEMBER);

        projectMemberRepository.update(projectMember);
    }

    private void checkProjectPermission(Long projectId,
                                        ProjectMemberRole projectMemberRole,
                                        String errorMessage) {

        ProjectMember projectMember = ProjectMember.builder()
                .projectId(projectId)
                .memberId(SecurityUtils.getCurrentUser().getId())
                .role(projectMemberRole)
                .build();
        ProjectMemberRole roleByMember = projectMemberRepository.findRoleByMember(projectMember);

        if (roleByMember == null || roleByMember.hasIssueAccess()) {
            throw new InsufficientPermissionException(errorMessage);
        }

    }

    private ProjectMember buildProjectMember(Long projectId, Long memberId, ProjectMemberRole roleType) {
        return ProjectMember.builder()
                .projectId(projectId)
                .memberId(memberId)
                .role(roleType)
                .build();
    }

}
