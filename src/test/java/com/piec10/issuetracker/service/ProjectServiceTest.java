package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();

    @Captor
    private ArgumentCaptor<Project> capturedProject;

    private FormProject formProject;

    private User createdBy;

    private Project project;

    private Date createdAt;

    private HashSet<User> followers;
    private HashSet<User> collaborators;

    @BeforeEach
    public void beforeEach() {

        formProject = new FormProject();

        formProject.setTitle("Title");
        formProject.setDescription("Description");

        createdBy = new User();
        createdBy.setUsername("user");

        createdAt = new Date(new Date().getTime() - 1000);

        project = new Project();
        project.setTitle("Old title");
        project.setDescription("Old description");
        project.setCreatedBy(createdBy);
        project.setCreatedAt(createdAt);

        followers = new HashSet<>();
        collaborators = new HashSet<>();

        followers.add(createdBy);
        collaborators.add(createdBy);

        project.setFollowers(followers);
        project.setCollaborators(collaborators);
    }

    @Test
    public void createNewValidProjectServiceEmptyFollowersAndCollaborators() {

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(formProject.getId(), capturedProject.getValue().getId(), "Project id should match and be 0 for new project");
        assertEquals(formProject.getTitle(), capturedProject.getValue().getTitle(), "Project title should match");
        assertEquals(formProject.getDescription(), capturedProject.getValue().getDescription(), "Project description should match");

        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCreatedBy().getUsername(), "Project creator name should match");

        assertNotNull(capturedProject.getValue().getCreatedAt(), "Project creation date should not be null");
        assertTrue(new Date(new Date().getTime()+10).getTime() > capturedProject.getValue().getCreatedAt().getTime(), "Project creation date should be older than current date");

        assertEquals(1, capturedProject.getValue().getFollowers().size(), "Should be only one follower - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getFollowers().stream().toList().get(0).getUsername(), "Should be only one follower - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCollaborators().stream().toList().get(0).getUsername(), "Should be only one collaborator - creator");
    }

    @Test
    public void createNewValidProjectServiceWithFollowersAndCollaborators() {

        User user2 = new User();
        user2.setUsername("user2");

        formProject.getFollowersNames().add(user2.getUsername());
        formProject.getCollaboratorsNames().add(user2.getUsername());

        when(userService.findByUsername("user2")).thenReturn(user2);

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(2, capturedProject.getValue().getFollowers().size(), "Should be two followers");
        assertEquals(2, capturedProject.getValue().getCollaborators().size(), "Should be two collaborators");
        assertTrue(capturedProject.getValue().getFollowers().contains(user2), "user2 should be follower");
        assertTrue(capturedProject.getValue().getCollaborators().contains(user2), "user2 should be collaborator");
    }

    @Test
    public void createNewValidProjectServiceWithCreatorAddedAsFollowerAndCollaborator() {

        formProject.getFollowersNames().add(createdBy.getUsername());
        formProject.getCollaboratorsNames().add(createdBy.getUsername());

        when(userService.findByUsername("user")).thenReturn(createdBy);

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(1, capturedProject.getValue().getFollowers().size(), "Should be only one follower - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
    }

    @Test
    public void updateValidProjectServiceNoFollowersAndCollaborators() {

        int id = 7;

        formProject.setId(id);
        project.setId(id);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        projectService.updateProject(formProject);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(formProject.getId(), capturedProject.getValue().getId(), "Project id should match");
        assertEquals(formProject.getTitle(), capturedProject.getValue().getTitle(), "Project title should be updated");
        assertEquals(formProject.getDescription(), capturedProject.getValue().getDescription(), "Project description should be updated");

        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCreatedBy().getUsername(), "Project creator name should not change");

        assertEquals(createdAt, capturedProject.getValue().getCreatedAt(), "Project creation date should not change");

        assertEquals(1, capturedProject.getValue().getFollowers().size(), "Should be only one follower - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getFollowers().stream().toList().get(0).getUsername(), "Should be only one follower - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCollaborators().stream().toList().get(0).getUsername(), "Should be only one collaborator - creator");
    }

    @Test
    public void updateValidProjectServiceWithFollowersAndCollaborators() {

        int id = 5;

        formProject.setId(id);
        project.setId(id);

        User user2 = new User();
        user2.setUsername("user2");

        formProject.getFollowersNames().add(user2.getUsername());
        formProject.getCollaboratorsNames().add(user2.getUsername());

        when(userService.findByUsername("user2")).thenReturn(user2);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        projectService.updateProject(formProject);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(2, capturedProject.getValue().getFollowers().size(), "Should be two followers");
        assertEquals(2, capturedProject.getValue().getCollaborators().size(), "Should be two collaborators");
        assertTrue(capturedProject.getValue().getFollowers().contains(user2), "user2 should be follower");
        assertTrue(capturedProject.getValue().getCollaborators().contains(user2), "user2 should be collaborator");
    }

    @Test
    public void updateValidProjectServiceWithFollowersAndCollaboratorsAndCreatorAdded() {

        int id = 5;

        formProject.setId(id);
        project.setId(id);

        User user2 = new User();
        user2.setUsername("user2");

        formProject.getFollowersNames().add(user2.getUsername());
        formProject.getCollaboratorsNames().add(user2.getUsername());
        formProject.getFollowersNames().add(createdBy.getUsername());
        formProject.getCollaboratorsNames().add(createdBy.getUsername());

        when(userService.findByUsername("user2")).thenReturn(user2);
        when(userService.findByUsername("user")).thenReturn(createdBy);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        projectService.updateProject(formProject);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(2, capturedProject.getValue().getFollowers().size(), "Should be two followers");
        assertEquals(2, capturedProject.getValue().getCollaborators().size(), "Should be two collaborators");
        assertTrue(capturedProject.getValue().getFollowers().contains(user2), "user2 should be follower");
        assertTrue(capturedProject.getValue().getCollaborators().contains(user2), "user2 should be collaborator");
    }
}
