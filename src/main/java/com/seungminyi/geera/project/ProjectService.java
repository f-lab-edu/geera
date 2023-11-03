package com.seungminyi.geera.project;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seungminyi.geera.exception.InsufficientPermissionException;
import com.seungminyi.geera.member.auth.CustomUserDetails;
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
            .role(ProjectMemberRoleType.CREATOR)
            .build();

        projectMemberRepository.create(projectMember);

        return project;
    }

    public List<Project> getProjects(String sortKey, String sortOrder, int page, int size) {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUser();

        if (page < 1) {
            page = 1;
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

    @ProjectPermissionCheck(ProjectMemberRoleType.CREATOR)
    public void updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRequest.toProject();
        project.setProjectId(projectId);
        projectRepository.update(project);
    }

    @ProjectPermissionCheck(ProjectMemberRoleType.CREATOR)
    public void deleteProject(Long projectId) {
        projectRepository.delete(projectId);
    }

    @ProjectPermissionCheck(ProjectMemberRoleType.CREATOR)
    public void addProjectMember(Long projectId, Long memberId) {
        ProjectMember projectMember = createProjectMember(projectId, ProjectMemberRoleType.INVITED);
        projectMemberRepository.create(projectMember);
    }

    @ProjectPermissionCheck(ProjectMemberRoleType.CREATOR)
    public int deleteProjectMember(Long projectId, Long memberId) {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.getId() == memberId) {
            throw new InsufficientPermissionException("본인을 프로젝트에서 제외할 수 없습니다.");
        }

        ProjectMember projectMember = createProjectMember(projectId, ProjectMemberRoleType.MEMBER);

        return projectMemberRepository.delete(projectMember);
    }

    public void acceptProjectInvitation(Long projectId) {
        checkProjectPermission(projectId, ProjectMemberRoleType.INVITED, "초대받지 않은 유저 입니다.");

        ProjectMember projectMember =
            createProjectMember(projectId, ProjectMemberRoleType.MEMBER);

        projectMemberRepository.update(projectMember);
    }

    private void checkProjectPermission(Long projectId,
        ProjectMemberRoleType projectMemberRoleType,
        String errorMessage) {

        ProjectMember projectMember = ProjectMember.builder()
            .projectId(projectId)
            .memberId(SecurityUtils.getCurrentUser().getId())
            .role(projectMemberRoleType)
            .build();
        ProjectMemberRoleType roleByMember = projectMemberRepository.findRoleByMember(projectMember);

        if (roleByMember == null) {
            throw new InsufficientPermissionException(errorMessage);
        }

    }

    private ProjectMember createProjectMember(Long projectId, ProjectMemberRoleType roleType) {
        return  ProjectMember.builder()
            .projectId(projectId)
            .memberId(SecurityUtils.getCurrentUser().getId())
            .role(roleType)
            .build();
    }

}
