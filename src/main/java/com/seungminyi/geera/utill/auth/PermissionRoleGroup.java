package com.seungminyi.geera.utill.auth;

import java.util.Set;

import com.seungminyi.geera.project.ProjectMemberRole;

public class PermissionRoleGroup {
    public static final Set<ProjectMemberRole> PROJECT_ACCESS_ROLES = Set.of(ProjectMemberRole.CREATOR);
    public static final Set<ProjectMemberRole> ISSUE_ACCESS_ROLES = Set.of(ProjectMemberRole.CREATOR,
        ProjectMemberRole.MEMBER);

}
