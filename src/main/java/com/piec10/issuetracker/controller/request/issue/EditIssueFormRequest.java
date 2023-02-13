package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.doesNotHavePermissionToModify;

public class EditIssueFormRequest extends RestrictedAccessIssueFormRequest {

    HttpServletRequest request;

    public EditIssueFormRequest(IssueService issueService, int issueId, HttpServletRequest request, Model model) {
        super(issueService, model);
        this.issue = issueService.findById(issueId);
        this.request = request;
    }

    @Override
    protected void prepareModelAttributes() {
        formIssue.setId(issue.getId());
        formIssue.setSummary(issue.getSummary());
        formIssue.setDescription(issue.getDescription());
        formIssue.setPriority(issue.getPriority());
        formIssue.setProjectId(issue.getProject().getId());

        List<Integer> issueTags = issue.getIssueTags().stream().map(issueTag -> issueTag.getId()).collect(Collectors.toList());
        formIssue.setIssueTags(issueTags);

        if (issue.getIssueType() != null) {
            formIssue.setIssueTypeId(issue.getIssueType().getId());
        }

        if (issue.getIssueStatus() != null) {
            formIssue.setIssueStatusId(issue.getIssueStatus().getId());
        }
    }

    @Override
    public boolean isNull() {
        return issue == null;
    }

    @Override
    public boolean hasNoPermission() {
        return doesNotHavePermissionToModify(issue, request);
    }
}
