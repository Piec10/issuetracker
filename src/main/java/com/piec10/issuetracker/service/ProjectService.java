package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;

import java.util.Collection;

public interface ProjectService {

    Project findById(int projectId);

    void createProject(FormProject formProject, User createdBy);

    Collection<Project> findAll();

    void updateProject(FormProject formProject);

    void deleteById(int projectId);
}
