package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
