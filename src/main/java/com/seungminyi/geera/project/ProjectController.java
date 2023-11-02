package com.seungminyi.geera.project;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.exception.ProjectPermissionException;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProject(
        @RequestParam(defaultValue = "project_Id") String sortKey,
        @RequestParam(defaultValue = "desc") String sortOrder,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {

        List<Project> projects = projectService.getProjects(sortKey, sortOrder, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest projectRequest) {
        Project createdProject = projectService.createProject(projectRequest.toProject());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest projectRequest) {
        projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 수정 완료.");
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 삭제 완료.");
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> addProjectMember(@PathVariable Long projectId,
        @PathVariable Long memberId) {
        try {
            projectService.addProjectMember(projectId, memberId);
            return ResponseEntity.status(HttpStatus.CREATED).body("맴버 초대 완료.");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("초대에 실패했습니다.");
        }
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable Long projectId,
        @PathVariable Long memberId) {
        int change = projectService.deleteProjectMember(projectId, memberId);
        if (change == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트에 소속된 맴버가 아닙니다.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("맴버 삭제 완료.");
    }

    @PutMapping("/{projectId}/accept-invitation")
    public ResponseEntity<?> acceptProjectInvitation(@PathVariable Long projectId) {
        projectService.acceptProjectInvitation(projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body("초대 수락 완료.");
    }
}
