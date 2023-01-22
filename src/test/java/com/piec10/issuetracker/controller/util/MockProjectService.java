package com.piec10.issuetracker.controller.util;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.ProjectService;

import static org.mockito.Mockito.when;

public abstract class MockProjectService {

    private static Project project = new Project();

    public static void mockSetup(ProjectService projectService) {
        project.setId(1);
        when(projectService.findById(project.getId())).thenReturn(project);
        when(projectService.findById(0)).thenReturn(null);
    }

    public static Project getProject() {
        return project;
    }
}
