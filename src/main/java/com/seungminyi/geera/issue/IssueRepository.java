package com.seungminyi.geera.issue;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.seungminyi.geera.issue.dto.Issue;
import com.seungminyi.geera.issue.dto.IssueAssignee;
import com.seungminyi.geera.issue.dto.IssueConditionsDto;

@Mapper
public interface IssueRepository {
    void create(Issue issue);

    List<Issue> getWithConditions(IssueConditionsDto params, RowBounds rowBounds);

    Long getProjectId(Long issueId);

    void update(Issue issue);

    void patch(Issue issue);

    void delete(Long issueId);

    void updateSubIssuesOnParentDeletion(Long issueId);

    void deleteAssignees(Long issueId);

    void insertAssignee(Long issueId, Long memberId);
}
