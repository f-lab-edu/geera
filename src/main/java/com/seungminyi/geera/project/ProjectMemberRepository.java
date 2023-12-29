package com.seungminyi.geera.project;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seungminyi.geera.member.dto.ProjectInfo;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.project.dto.ProjectTeamMember;

@Mapper
public interface ProjectMemberRepository {
    Long create(ProjectMember projectMember);

    ProjectMemberRole findRoleByMember(ProjectMember projectMember);

    int delete(ProjectMember projectMember);

    void update(ProjectMember projectMember);

    List<ProjectInfo> getInvitedProjects(Long memberId);

    List<ProjectTeamMember> getAllByProject(Long projectId);
}
