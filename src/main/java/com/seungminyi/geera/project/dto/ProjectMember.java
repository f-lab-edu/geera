package com.seungminyi.geera.project.dto;

import com.seungminyi.geera.project.ProjectMemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class ProjectMember {
    private Long projectId;
    private Long memberId;
    private ProjectMemberRole role;
}
