package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.controller.request.RequestRedirections.*;
import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.isNotProjectCollaborator;

public class CreateIssueRequest implements RestrictedAccessRequest {

    private IssueService issueService;
    private FormIssue formIssue;
    private User requestUser;
    private Project project;
    private HttpServletRequest request;

    public CreateIssueRequest(IssueService issueService, Project project, HttpServletRequest request, User requestUser, FormIssue formIssue) {
        this.issueService = issueService;
        this.project = project;
        this.request = request;
        this.requestUser = requestUser;
        this.formIssue = formIssue;
    }

    @Override
    public boolean isNull() {
        return (project == null) || (formIssue.getId() != 0);
    }

    @Override
    public String redirectWhenNull() {
        return toProjects();
    }

    @Override
    public boolean hasNoPermission() {
        return isNotProjectCollaborator(requestUser, project);
    }

    @Override
    public String redirectWhenNoPermission() {
        return toAccessDenied();
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
