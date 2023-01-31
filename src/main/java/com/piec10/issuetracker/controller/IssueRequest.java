package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isAdmin;
import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isOwner;

public abstract class IssueRequest implements Request{

    protected Issue issue;
    private HttpServletRequest request;

    public IssueRequest(Issue issue, HttpServletRequest request) {
        this.issue = issue;
        this.request = request;
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
