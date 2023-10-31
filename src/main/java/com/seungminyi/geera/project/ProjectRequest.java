package com.seungminyi.geera.project;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.seungminyi.geera.member.Member;

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
