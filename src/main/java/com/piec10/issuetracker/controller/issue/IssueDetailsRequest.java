package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import org.springframework.ui.Model;


import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

public class IssueDetailsRequest extends IssueRequest {

    private final User requestUser;
    private final Model model;

    public IssueDetailsRequest(Issue issue, User requestUser, Model model) {
        this.issue = issue;
        this.requestUser = requestUser;
        this.model = model;
    }

    @Override
    public String processRequest() {

        if (issue == null) return toProjects();

        Project project = issue.getProject();
        if (project == null) return toProjects();

        if (isNotAdminOrProjectFollower(requestUser, project)) return toAccessDenied();

        model.addAttribute("issue", issue);

        return "dashboard/issue-details";
    }
}
