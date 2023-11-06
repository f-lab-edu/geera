package com.seungminyi.geera.project.dto;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Validated
@Data
public class ProjectRequest {
    private Long projectId;
    @NotBlank
    private String projectName;

    public Project toProject() {
        Project project = new Project();
        project.setProjectName(this.projectName);
        return project;
    }
}
