package com.piec10.issuetracker.controller.request.project;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.controller.request.RestrictedAccessRequestStrategy;
import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProjectRequestFactoryImpl implements ProjectRequestFactory{

    @Autowired
    private ProjectService projectService;

    @Override
    public Request createDeleteProjectRequest(int projectId, HttpServletRequest request) {

        RestrictedAccessRequest deleteProjectRequest = new DeleteProjectRequest(projectService, projectId, request);

        return new RestrictedAccessRequestStrategy(deleteProjectRequest);
    }

    @Override
    public Request createEditProjectRequest(int projectId, HttpServletRequest request, Model model) {

        RestrictedAccessRequest editProjectRequest = new EditProjectRequest(projectService, projectId, request, model);

        return new RestrictedAccessRequestStrategy(editProjectRequest);
    }
}
