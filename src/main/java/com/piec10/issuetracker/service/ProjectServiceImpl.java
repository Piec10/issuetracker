package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project findById(int projectId) {

        Optional<Project> project = projectRepository.findById(projectId);

        if(project.isPresent()){
            return project.get();
        }
        else return null;
    }

    @Override
    public void createProject(FormProject formProject, User createdBy) {

        Project newProject = new Project();
        newProject.setTitle(formProject.getTitle());
        newProject.setDescription(formProject.getDescription());
        newProject.setCreatedBy(createdBy);
        newProject.setCreatedAt(new Date());
        newProject.setGuestUsers(Arrays.asList(createdBy));
        newProject.setCollaborators(Arrays.asList(createdBy));

        projectRepository.save(newProject);
    }
}
