package com.seungminyi.geera.project;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectRepository {
    Long create(Project project);

    List<Project> findByMember(ProjectQuery query);

    void update(Project project);

    void delete(Long projectId);
}
