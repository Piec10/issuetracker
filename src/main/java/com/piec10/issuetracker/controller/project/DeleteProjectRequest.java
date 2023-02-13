package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.service.ProjectService;

import javax.servlet.http.HttpServletRequest;

public class DeleteProjectRequest extends ProjectRestrictedAccessRequest {


    public DeleteProjectRequest(ProjectService projectService, int projectId, HttpServletRequest request) {
        super(projectService, projectId, request);
    }

    @Override
    public void doWork() {
        projectService.deleteById(project.getId());
    }

    @Override
    public String redirectWhenSuccess() {
        return "redirect:/dashboard/projects";
    }

}
