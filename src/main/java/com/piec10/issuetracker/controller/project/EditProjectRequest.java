package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class EditProjectRequest extends ProjectModificationRequest{

    private Model model;

    public EditProjectRequest(ProjectService projectService, int projectId, HttpServletRequest request, Model model) {
        super(projectService, projectId, request);
        this.model = model;
    }

    @Override
    public void modify() {
        FormProject formProject = new FormProject();

        formProject.setId(project.getId());
        formProject.setTitle(project.getTitle());
        formProject.setDescription(project.getDescription());
        formProject.setFollowersNamesFromUsers(project.getFollowers());
        formProject.setCollaboratorsNamesFromUsers(project.getCollaborators());

        model.addAttribute("formProject", formProject);
    }

    @Override
    public String redirectWhenSuccess() {
        return "dashboard/project-form";
    }
}
