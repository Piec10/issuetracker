package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
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
    public Collection<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public void createProject(FormProject formProject, User createdBy) {

        Project newProject = new Project();
        newProject.setTitle(formProject.getTitle());
        newProject.setDescription(formProject.getDescription());
        newProject.setCreatedBy(createdBy);
        newProject.setCreatedAt(new Date());
        formProject.getGuestUsers().add(createdBy);
        formProject.getCollaborators().add(createdBy);

        newProject.setGuestUsers(formProject.getGuestUsers());
        newProject.setCollaborators(formProject.getCollaborators());

        projectRepository.save(newProject);
    }

    @Override
    public void updateProject(FormProject formProject) {

        Project project = findById(formProject.getId());

        if(project != null) {

            project.setTitle(formProject.getTitle());
            project.setDescription(formProject.getDescription());

            //add owner if he deleted himself
            if(!formProject.getCollaborators().contains(project.getCreatedBy())){
                formProject.getCollaborators().add(project.getCreatedBy());
            }
            if(!formProject.getGuestUsers().contains(project.getCreatedBy())){
                formProject.getGuestUsers().add(project.getCreatedBy());
            }

            project.setGuestUsers(formProject.getGuestUsers());
            project.setCollaborators(formProject.getCollaborators());

            projectRepository.save(project);
        }
    }
}
