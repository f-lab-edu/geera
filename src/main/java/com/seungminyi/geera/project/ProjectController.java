package com.seungminyi.geera.project;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seungminyi.geera.common.dto.ResponseMessage;
import com.seungminyi.geera.project.dto.Project;
import com.seungminyi.geera.project.dto.ProjectRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Authorization")
@Tag(name = "프로젝트")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "프로젝트 조회")
    @Parameter(name = "sortKey", description = "project_id, project_name, create_at")
    @ApiResponse(responseCode = "200", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Project.class)), mediaType = "application/json")
    })
    @GetMapping
    public ResponseEntity<List<Project>> getProjects(
        @RequestParam(defaultValue = "project_id") String sortKey,
        @RequestParam(defaultValue = "desc") String sortOrder,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<Project> projects = projectService.getProjects(sortKey, sortOrder, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @Operation(summary = "프로젝트 생성")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")})
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest projectRequest) {
        Project createdProject = projectService.createProject(projectRequest.toProject());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @Operation(summary = "프로젝트 수정")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest projectRequest) {
        projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("프로젝트 수정 완료."));
    }

    @Operation(summary = "프로젝트 삭제")
    @ApiResponse(responseCode = "204", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage("프로젝트 삭제 완료."));
    }

    @Operation(summary = "프로젝트 맴버 초대")
    @ApiResponses({
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "409", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    })
    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> addProjectMember(@PathVariable Long projectId,
        @PathVariable Long memberId) {
        try {
            projectService.addProjectMember(projectId, memberId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("맴버 초대 완료."));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("초대에 실패했습니다."));
        }
    }

    @Operation(summary = "프로젝트 맴버 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", content = {
            @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    })
    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable Long projectId,
        @PathVariable Long memberId) {
        int change = projectService.deleteProjectMember(projectId, memberId);
        if (change == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("프로젝트에 소속된 맴버가 아닙니다."));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage("맴버 삭제 완료."));
    }

    @Operation(summary = "프로젝트 초대 수락")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = ResponseMessage.class), mediaType = "application/json")})
    @PatchMapping("/{projectId}/accept-invitation")
    public ResponseEntity<?> acceptProjectInvitation(@PathVariable Long projectId) {
        projectService.acceptProjectInvitation(projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body("초대 수락 완료.");
    }
}
