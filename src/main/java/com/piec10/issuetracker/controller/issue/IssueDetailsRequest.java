package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import org.springframework.ui.Model;


import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

public class IssueDetailsRequest extends IssueRequest {

    private User currentUser;
    private Model model;

    public IssueDetailsRequest(Issue issue, User currentUser, Model model) {
        this.issue = issue;
        this.currentUser = currentUser;
        this.model = model;
    }

    @Override
    public String processRequest() {

        if (issue == null) return toProjects();

        Project project = issue.getProject();
        if (project == null) return toProjects();

        if (isNotAdminOrProjectFollower(currentUser, project)) return toAccessDenied();

        model.addAttribute("issue", issue);

        return "dashboard/issue-details";
    }
}
