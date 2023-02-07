package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

public abstract class IssueModificationRequest extends IssueRequest {

    protected IssueService issueService;
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

    private static String toCurrentProject(Project project) {
        return "redirect:/dashboard/issues?projectId=" + project.getId();
    }
}
