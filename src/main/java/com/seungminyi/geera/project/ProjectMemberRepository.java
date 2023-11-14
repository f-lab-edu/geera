package com.seungminyi.geera.project;

import org.apache.ibatis.annotations.Mapper;

import com.seungminyi.geera.project.dto.ProjectMember;

@Mapper
public interface ProjectMemberRepository {
    Long create(ProjectMember projectMember);

    ProjectMemberRole findRoleByMember(ProjectMember projectMember);

    int delete(ProjectMember projectMember);

    void update(ProjectMember projectMember);
}
