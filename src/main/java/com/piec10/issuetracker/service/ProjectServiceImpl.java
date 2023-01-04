package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

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

        Collection<User> followers = getFromNames(formProject.getFollowersNames());
        Collection<User> collaborators = getFromNames(formProject.getCollaboratorsNames());

        followers.add(createdBy);
        collaborators.add(createdBy);

        newProject.setFollowers(followers);
        newProject.setCollaborators(collaborators);

        projectRepository.save(newProject);
    }


    @Override
    public void updateProject(FormProject formProject) {

        Project project = findById(formProject.getId());

        if(project != null) {

            project.setTitle(formProject.getTitle());
            project.setDescription(formProject.getDescription());

            Collection<User> followers = getFromNames(formProject.getFollowersNames());
            Collection<User> collaborators = getFromNames(formProject.getCollaboratorsNames());

            //add owner if he deleted himself
            if(!followers.contains(project.getCreatedBy())){
                followers.add(project.getCreatedBy());
            }
            if(!collaborators.contains(project.getCreatedBy())){
                collaborators.add(project.getCreatedBy());
            }

            project.setFollowers(followers);
            project.setCollaborators(collaborators);

            projectRepository.save(project);
        }
    }

    @Override
    public void deleteById(int projectId) {
        projectRepository.deleteById(projectId);
    }

    private Collection<User> getFromNames(Collection<String> usersNames) {

        Collection<User> users = new HashSet<>();

        for(String userName : usersNames) {

            User user = userService.findByUsername(userName);

            if(user != null) users.add(user);
        }

        return users;
    }
}
