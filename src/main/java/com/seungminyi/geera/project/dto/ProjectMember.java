package com.seungminyi.geera.project.dto;

import com.seungminyi.geera.project.ProjectMemberRoleType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProjectMember {
    private Long projectId;
    private Long memberId;
    private ProjectMemberRoleType role;
}
