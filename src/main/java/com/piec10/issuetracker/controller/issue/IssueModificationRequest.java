package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isAdmin;
import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isOwner;

public abstract class IssueModificationRequest implements Request {

    protected IssueService issueService;
    protected Issue issue;
    private HttpServletRequest request;

    public IssueModificationRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        this.issueService = issueService;
        this.request = request;
        issue = issueService.findById(issueId);
    }

    public String processRequest() {

        if (issue == null) return toProjects();
        if (doesNotHavePermissionToModify(issue, request)) return toAccessDenied();

        modifyIssue();

        return toCurrentProject(issue.getProject());
    }

    public abstract void modifyIssue();

    private static boolean doesNotHavePermissionToModify(Issue issue, HttpServletRequest request) {
        return !hasPermissionToModify(issue, request);
    }

    private static boolean hasPermissionToModify(Issue issue, HttpServletRequest request) {
        return isOwner(issue.getProject(), request) || isOwner(issue, request) || isAdmin(request);
    }

    private static String toProjects() {
        return "redirect:/dashboard/projects";
    }

    private static String toCurrentProject(Project project) {
        return "redirect:/dashboard/issues?projectId=" + project.getId();
    }

    private static String toAccessDenied() {
        return "redirect:/access-denied";
    }
}
