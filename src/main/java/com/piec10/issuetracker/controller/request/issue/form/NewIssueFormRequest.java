package com.piec10.issuetracker.controller.request.issue.form;

import com.piec10.issuetracker.controller.request.issue.form.RestrictedAccessIssueFormRequest;
import com.piec10.issuetracker.entity.IssueStatus;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.isNotProjectCollaborator;

public class NewIssueFormRequest extends RestrictedAccessIssueFormRequest {

    private Project project;
    private User requestUser;

    public NewIssueFormRequest(IssueService issueService, Project project, User requestUser, Model model) {
        super(issueService, model);
        this.project = project;
        this.requestUser = requestUser;
    }

    @Override
    protected void prepareModelAttributes() {

        formIssue.setProjectId(project.getId());

        IssueStatus done = issueService.findIssueStatusByName("Done");
        if (done != null) {
            allIssueStatuses.remove(done);
        }
    }

    @Override
    public boolean isNull() {
        return project == null;
    }

    @Override
    public boolean hasNoPermission() {
        return isNotProjectCollaborator(requestUser, project);
    }
}
