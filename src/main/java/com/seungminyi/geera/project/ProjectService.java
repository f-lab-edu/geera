package com.seungminyi.geera.project;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seungminyi.geera.exception.ProjectPermissionException;
import com.seungminyi.geera.member.auth.CustomUserDetails;

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
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.setCreateAt(new Date());
        projectRepository.create(project);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(project.getProjectId());
        projectMember.setMemberId(userDetails.getId());
        projectMember.setRole(ProjectMemberRoleType.CREATOR);

        projectMemberRepository.create(projectMember);

        return project;
    }

    public List<Project> getProjects() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectRepository.findByMember(userDetails.getId());
    }

    public void updateProject(Long projectId, ProjectRequest projectRequest) {
        checkProjectPermission(projectId,ProjectMemberRoleType.CREATOR,"프로젝트 생성자만 요청할 수 있습니다.");


        Project project = projectRequest.toProject();
        project.setProjectId(projectId);
        projectRepository.update(project);
    }

    public void deleteProject(Long projectId) {
        checkProjectPermission(projectId,ProjectMemberRoleType.CREATOR,"프로젝트 생성자만 요청할 수 있습니다.");

        projectRepository.delete(projectId);
    }

    public void addProjectMember(Long projectId, Long memberId) {
        checkProjectPermission(projectId,ProjectMemberRoleType.CREATOR,"프로젝트 생성자만 요청할 수 있습니다.");

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setMemberId(memberId);
        projectMember.setRole(ProjectMemberRoleType.INVITED);

        projectMemberRepository.create(projectMember);
    }

    public int deleteProjectMember(Long projectId, Long memberId) {
        CustomUserDetails currentUser = getCurrentUser();
        if (currentUser.getId() == memberId) {
            throw new ProjectPermissionException("본인을 프로젝트에서 제외할 수 없습니다.");
        }
        checkProjectPermission(projectId,ProjectMemberRoleType.CREATOR,"프로젝트 생성자만 요청할 수 있습니다.");

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setMemberId(memberId);


        return projectMemberRepository.delete(projectMember);
    }

    public void acceptProjectInvitation(Long projectId) {
        checkProjectPermission(projectId, ProjectMemberRoleType.INVITED, "초대받지 않은 유저 입니다.");

        CustomUserDetails currentUser = getCurrentUser();
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setMemberId(currentUser.getId());
        projectMember.setRole(ProjectMemberRoleType.MEMBER);

        projectMemberRepository.update(projectMember);
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkProjectPermission(Long projectId,ProjectMemberRoleType projectMemberRoleType, String errorMessage) {
        CustomUserDetails userDetails = getCurrentUser();
        ProjectMember projectMember = new ProjectMember();
        projectMember.setMemberId(userDetails.getId());
        projectMember.setProjectId(projectId);
        projectMember.setRole(projectMemberRoleType);
        System.out.println(userDetails.getEmail());
        ProjectMemberRoleType roleByMember = projectMemberRepository.findRoleByMember(projectMember);
        if (roleByMember == null) {
            throw new ProjectPermissionException(errorMessage);
        }
    }


}