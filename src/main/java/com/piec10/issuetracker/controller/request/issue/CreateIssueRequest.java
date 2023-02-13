package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.controller.request.RequestRedirections.toCurrentProject;

public class CreateIssueRequest implements RestrictedAccessRequest {

    protected IssueService issueService;
    protected Issue issue;
    private FormIssue formIssue;
    private User requestUser;
    private Project project;
    private HttpServletRequest request;

    public CreateIssueRequest(IssueService issueService, Project project, HttpServletRequest request, FormIssue formIssue) {
        this.issueService = issueService;
        this.project = project;
        this.request = request;
        this.formIssue = formIssue;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public String redirectWhenNull() {
        return null;
    }

    @Override
    public boolean hasNoPermission() {
        return false;
    }

    @Override
    public String redirectWhenNoPermission() {
        return null;
    }

    @Override
    public void doWork() {
        issueService.createIssue(formIssue, requestUser, project);
    }

    @Override
    public String redirectWhenSuccess() {
        return toCurrentProject(project);
    }
}
