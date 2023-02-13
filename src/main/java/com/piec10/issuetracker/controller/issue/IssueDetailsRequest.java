package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import org.springframework.ui.Model;


import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

public class IssueDetailsRequest extends IssueRequest implements RestrictedAccessRequest {

    private final User requestUser;
    private final Model model;

    private Project project;

    public IssueDetailsRequest(Issue issue, User requestUser, Model model) {
        this.issue = issue;
        this.requestUser = requestUser;
        this.model = model;

        if(issue != null) project = issue.getProject();
    }

    @Override
    public boolean isNull() {
        return (issue == null) || (project == null);
    }

    @Override
    public String redirectWhenNull() {
        return toProjects();
    }

    @Override
    public boolean hasNoPermission() {
        return isNotAdminOrProjectFollower(requestUser, project);
    }

    @Override
    public String redirectWhenNoPermission() {
        return toAccessDenied();
    }

    @Override
    public void doWork() {
        model.addAttribute("issue", issue);
    }

    @Override
    public String redirectWhenSuccess() {
        return "dashboard/issue-details";
    }
}
