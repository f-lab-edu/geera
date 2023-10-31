package com.seungminyi.geera.project;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;


@Data
public class ProjectMember {
    private Long projectId;
    private Long memberId;
    private ProjectMemberRoleType role;
}
