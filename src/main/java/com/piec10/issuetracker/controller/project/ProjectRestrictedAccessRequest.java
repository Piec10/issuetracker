package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.ProjectService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isNotAdminOrOwner;

public abstract class ProjectRestrictedAccessRequest implements RestrictedAccessRequest {

    protected ProjectService projectService;
    protected Project project;
    protected HttpServletRequest request;

    public ProjectRestrictedAccessRequest(ProjectService projectService, int projectId, HttpServletRequest request) {
        this.projectService = projectService;
        project = projectService.findById(projectId);
        this.request = request;
    }

    @Override
    public boolean isNull() {
        return project == null;
    }

    @Override
    public String redirectWhenNull() {
        return "redirect:/dashboard/projects";
    }

    @Override
    public boolean hasNoPermission() {
        return isNotAdminOrOwner(project, request);
    }

    @Override
    public String redirectWhenNoPermission() {
        return "redirect:/access-denied";
    }
}
