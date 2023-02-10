package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.controller.request.ModificationRequest;
import com.piec10.issuetracker.controller.request.ModificationRequestStrategy;
import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProjectRequestFactoryImpl implements ProjectRequestFactory{

    @Autowired
    private ProjectService projectService;

    @Override
    public Request createDeleteProjectRequest(int projectId, HttpServletRequest request) {

        ModificationRequest deleteProjectRequest = new DeleteProjectRequest(projectService, projectId, request);

        return new ModificationRequestStrategy(deleteProjectRequest);
    }
}
