package com.seungminyi.geera.issue;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.ibatis.session.RowBounds;

import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.generator.SqlGenerator;
import com.seungminyi.geera.core.gql.parser.GqlParser;
import com.seungminyi.geera.exception.MaxItemsExceededException;
import com.seungminyi.geera.exception.UnauthorizedException;
import com.seungminyi.geera.issue.dto.IssueAssignee;
import com.seungminyi.geera.issue.dto.IssueConditionsDto;
import com.seungminyi.geera.issue.dto.Issue;
import com.seungminyi.geera.issue.dto.IssueRequest;
import com.seungminyi.geera.project.ProjectMemberRepository;
import com.seungminyi.geera.project.ProjectMemberRole;
import com.seungminyi.geera.project.dto.ProjectMember;
import com.seungminyi.geera.utill.annotation.IssuePermissionCheck;
import com.seungminyi.geera.utill.auth.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @IssuePermissionCheck
    @Transactional
    public void createIssue(IssueRequest issueRequest) {
        validateAssignmentPermission(issueRequest);
        Issue issue = issueRequest.toIssue();
        issue.setIssueReporterId(SecurityUtils.getCurrentUser().getId());
        issue.setCreateAt(LocalDateTime.now());

        issueRepository.create(issue);

        Long createdIssueId = issue.getIssueId();

        Optional.ofNullable(issueRequest.getAssignees())
            .orElse(Collections.emptyList())
            .forEach(assignee -> issueRepository.insertAssignee(createdIssueId, assignee.getMemberId()));
    }

    public List<Issue> getIssuesWithConditions(
        Long project,
        int page,
        int limit,
        String sort,
        String order,
        String customSqlCondition) throws IOException {

        if (limit > 50) {
            throw new MaxItemsExceededException();
        }

        IssueConditionsDto issueConditionsDto = buildIssueConditionsDto(project, sort, order, customSqlCondition);
        RowBounds rowBounds = new RowBounds((page - 1) * limit, limit);

        return issueRepository.getWithConditions(issueConditionsDto, rowBounds);
    }

    @IssuePermissionCheck
    @Transactional
    public void deleteIssue(Long issueId) {
        issueRepository.deleteAssignees(issueId);
        issueRepository.updateSubIssuesOnParentDeletion(issueId);
        issueRepository.delete(issueId);
    }

    @IssuePermissionCheck
    public void putIssue(Long issueId, IssueRequest issueRequest) {
        validateAssignmentPermission(issueRequest);
        Issue issue = issueRequest.toIssue();
        issue.setIssueId(issueId);
        issue.setUpdateAt(LocalDateTime.now());
        issueRepository.update(issue);

        issueRepository.deleteAssignees(issueId);
        for (IssueAssignee assignee : issueRequest.getAssignees()) {
            issueRepository.insertAssignee(issueId, assignee.getMemberId());
        }
    }

    @IssuePermissionCheck
    public void patchIssue(Long issueId, IssueRequest issueRequest) {
        validateAssignmentPermission(issueRequest);
        Issue issue = issueRequest.toIssue();
        issue.setIssueId(issueId);
        issue.setUpdateAt(LocalDateTime.now());
        issueRepository.patch(issue);

        if (issueRequest.getAssignees() != null) {
            issueRepository.deleteAssignees(issueId);
            for (IssueAssignee assignee : issueRequest.getAssignees()) {
                issueRepository.insertAssignee(issueId, assignee.getMemberId());
            }
        }
    }

    private void validateAssignmentPermission(IssueRequest issueRequest) {
        List<IssueAssignee> assignees = issueRequest.getAssignees();
        if (assignees == null) {
            assignees = Collections.emptyList();
        }

        for (IssueAssignee assignee : assignees) {
            if (assignee.getMemberId() != null) {
                checkMemberHasIssueAccess(issueRequest.getProjectId(), assignee.getMemberId());
            }
        }
    }

    private void checkMemberHasIssueAccess(Long projectId, Long issueContractId) {
        ProjectMember projectMember = new ProjectMember()
            .setProjectId(projectId)
            .setMemberId(issueContractId);
        ProjectMemberRole memberRole = projectMemberRepository.findRoleByMember(projectMember);

        if (!memberRole.hasIssueAccess()) {
            throw new UnauthorizedException("담당자가 프로젝트에 권한이 없습니다.");
        }
    }

    private IssueConditionsDto buildIssueConditionsDto(Long project, String sort, String order,
        String customSqlCondition) throws IOException {
        IssueConditionsDto.IssueConditionsDtoBuilder builder = IssueConditionsDto.builder()
            .project(project)
            .sort(sort)
            .order(order)
            .member(SecurityUtils.getCurrentUser().getId());

        if (!customSqlCondition.isBlank()) {
            GqlParser gqlParser = new GqlParser(customSqlCondition);
            SqlGenerator generator = new SqlGenerator(Issue.class);
            GeeraQuery parseTree = gqlParser.parse();
            String visit = generator.visit(parseTree);
            builder.customSqlCondition(visit);
        }

        return builder.build();
    }
}
