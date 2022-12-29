package com.piec10.issuetracker;

import com.piec10.issuetracker.dao.ProjectRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProjectServiceTest {

    @MockBean
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    @Captor
    private ArgumentCaptor<Project> capturedProject;

    private FormProject formProject;

    private User createdBy;

    @BeforeEach
    public void beforeEach() {

        formProject = new FormProject();

        formProject.setTitle("Title");
        formProject.setDescription("Description");

        createdBy = new User();
        createdBy.setUsername("user");

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

}
