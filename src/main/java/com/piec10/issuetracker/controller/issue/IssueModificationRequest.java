package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.request.ModificationRequest;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

public abstract class IssueModificationRequest extends IssueRequest implements ModificationRequest {

    protected IssueService issueService;
    private HttpServletRequest request;

    public IssueModificationRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        this.issueService = issueService;
        this.request = request;
        issue = issueService.findById(issueId);
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

    private static String toCurrentProject(Project project) {
        return "redirect:/dashboard/issues?projectId=" + project.getId();
    }
}
