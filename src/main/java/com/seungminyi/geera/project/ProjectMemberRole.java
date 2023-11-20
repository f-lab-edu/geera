package com.seungminyi.geera.project;

import java.util.Set;

public enum ProjectMemberRole {
    CREATOR,
    MEMBER,
    INVITED;
    private static final Set<ProjectMemberRole> ISSUE_ACCESS_ROLES = Set.of(CREATOR, MEMBER);
    private static final Set<ProjectMemberRole> PROJECT_ACCESS_ROLES = Set.of(CREATOR);
    public boolean hasIssueAccess() {
        return ISSUE_ACCESS_ROLES.contains(this);
    }

    public boolean hasProjectAccess() {
        return PROJECT_ACCESS_ROLES.contains(this);
    }

}
