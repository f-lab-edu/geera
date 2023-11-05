package com.seungminyi.geera.project;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMemberRepository {
    Long create(ProjectMember projectMember);

    ProjectMemberRoleType findRoleByMember(ProjectMember projectMember);

    int delete(ProjectMember projectMember);

    void update(ProjectMember projectMember);
}
