package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.controller.request.ModificationRequest;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.ProjectService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isNotAdminOrOwner;

public class DeleteProjectRequest implements ModificationRequest {

    private ProjectService projectService;
    private Project project;
    private HttpServletRequest request;

    public DeleteProjectRequest(ProjectService projectService, int projectId, HttpServletRequest request) {
        this.projectService = projectService;
        this.request = request;
        project = projectService.findById(projectId);
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

    @Override
    public void modify() {
        projectService.deleteById(project.getId());
    }

    @Override
    public String redirectWhenSuccess() {
        return "redirect:/dashboard/projects";
    }
}
