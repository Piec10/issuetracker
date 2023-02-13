package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.controller.request.RequestRedirections.*;
import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.doesNotHavePermissionToModify;

public abstract class IssueModificationRequest implements RestrictedAccessRequest {

    protected IssueService issueService;
    protected Issue issue;
    private HttpServletRequest request;

    public IssueModificationRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        this.issueService = issueService;
        issue = issueService.findById(issueId);
        this.request = request;
    }

    @Override
    public boolean isNull() {
        return issue == null;
    }

    @Override
    public String redirectWhenNull() {
        return toProjects();
    }

    @Override
    public boolean hasNoPermission() {
        return doesNotHavePermissionToModify(issue, request);
    }

    @Override
    public String redirectWhenNoPermission() {
        return toAccessDenied();
    }

    @Override
    public String redirectWhenSuccess() {
        return toCurrentProject(issue.getProject());
    }
}
