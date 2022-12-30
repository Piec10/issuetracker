package com.piec10.issuetracker;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService = new ProjectServiceImpl();

    @Captor
    private ArgumentCaptor<Project> capturedProject;

    private FormProject formProject;

    private User createdBy;

    private Project project;

    private Date createdAt;

    private HashSet<User> guests;
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

        guests = new HashSet<>();
        collaborators = new HashSet<>();

        guests.add(createdBy);
        collaborators.add(createdBy);

        project.setGuestUsers(guests);
        project.setCollaborators(collaborators);
    }

    @Test
    public void createNewValidProjectServiceEmptyGuestsAndCollaborators() {

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(formProject.getId(), capturedProject.getValue().getId(), "Project id should match and be 0 for new project");
        assertEquals(formProject.getTitle(), capturedProject.getValue().getTitle(), "Project title should match");
        assertEquals(formProject.getDescription(), capturedProject.getValue().getDescription(), "Project description should match");

        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCreatedBy().getUsername(), "Project creator name should match");

        assertNotNull(capturedProject.getValue().getCreatedAt(), "Project creation date should not be null");
        assertTrue(new Date().getTime() > capturedProject.getValue().getCreatedAt().getTime(), "Project creation date should be older than current date");

        assertEquals(1, capturedProject.getValue().getGuestUsers().size(), "Should be only one guest user - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getGuestUsers().stream().toList().get(0).getUsername(), "Should be only one guest user - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCollaborators().stream().toList().get(0).getUsername(), "Should be only one collaborator - creator");
    }

    @Test
    public void createNewValidProjectServiceWithGuestsAndCollaborators() {

        User user2 = new User();
        user2.setUsername("user2");

        formProject.getGuestUsers().add(user2);
        formProject.getCollaborators().add(user2);

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(2, capturedProject.getValue().getGuestUsers().size(), "Should be two guest users");
        assertEquals(2, capturedProject.getValue().getCollaborators().size(), "Should be two collaborators");
        assertTrue(capturedProject.getValue().getGuestUsers().contains(user2), "user2 should be guest");
        assertTrue(capturedProject.getValue().getCollaborators().contains(user2), "user2 should be collaborator");
    }

    @Test
    public void createNewValidProjectServiceWithCreatorAddedAsGuestAndCollaborator() {

        formProject.getGuestUsers().add(createdBy);
        formProject.getCollaborators().add(createdBy);

        projectService.createProject(formProject,createdBy);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(1, capturedProject.getValue().getGuestUsers().size(), "Should be only one guest user - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
    }

    @Test
    public void updateValidProjectServiceNoGuestsAndCollaborators() {

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

        assertEquals(1, capturedProject.getValue().getGuestUsers().size(), "Should be only one guest user - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getGuestUsers().stream().toList().get(0).getUsername(), "Should be only one guest user - creator");
        assertEquals(1, capturedProject.getValue().getCollaborators().size(), "Should be only one collaborator - creator");
        assertEquals(createdBy.getUsername(), capturedProject.getValue().getCollaborators().stream().toList().get(0).getUsername(), "Should be only one collaborator - creator");
    }

    @Test
    public void updateValidProjectServiceWithGuestsAndCollaborators() {

        int id = 5;

        formProject.setId(id);
        project.setId(id);

        User user2 = new User();
        user2.setUsername("user2");

        formProject.getGuestUsers().add(user2);
        formProject.getCollaborators().add(user2);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        projectService.updateProject(formProject);

        verify(projectRepository).save(capturedProject.capture());

        assertEquals(2, capturedProject.getValue().getGuestUsers().size(), "Should be two guest users");
        assertEquals(2, capturedProject.getValue().getCollaborators().size(), "Should be two collaborators");
        assertTrue(capturedProject.getValue().getGuestUsers().contains(user2), "user2 should be guest");
        assertTrue(capturedProject.getValue().getCollaborators().contains(user2), "user2 should be collaborator");
    }
}
