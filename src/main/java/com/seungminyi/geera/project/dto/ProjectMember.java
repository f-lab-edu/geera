package com.seungminyi.geera.project.dto;

import com.seungminyi.geera.project.ProjectMemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectMember {
    private Long projectId;
    private Long memberId;
    private ProjectMemberRole role;
}
