package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectService projectService;

    private static User user;

    private static Project project;

    private static FormProject formProject;

    @BeforeAll
    public static void beforeAll() {

        formProject = new FormProject();
        user = new User();
        user.setUsername("user");
        project = new Project();
        project.setCreatedBy(user);
    }

    @Test
    public void getProjectsIsAdmin() throws Exception {

        when(userService.findByUsername("admin")).thenReturn(new User());

        mockMvc.perform(get("/dashboard/projects")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/projects"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("projects"));

        verify(projectService).findAll();
    }

    @Test
    public void getProjectsIsUser() throws Exception {

        user = mock(User.class);
        user.setGuestProjects(new ArrayList<>());

        when(userService.findByUsername("user")).thenReturn(user);

        mockMvc.perform(get("/dashboard/projects")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/projects"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("projects"));

        verify(user).getGuestProjects();
    }

    @Test
    public void getNewProjectFormIsUser() throws Exception {

        mockMvc.perform(get("/dashboard/newProject")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/project-form"))
                .andExpect(model().attributeExists("formProject"));
    }

    @Test
    public void getNewProjectFormIsGuest() throws Exception {

        mockMvc.perform(get("/dashboard/newProject")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER", "GUEST")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getEditProjectFormInvalidProjectId() throws Exception {

        when(projectService.findById(0)).thenReturn(null);

        mockMvc.perform(get("/dashboard/editProject")
                        .param("projectId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getEditProjectFormIsOwner() throws Exception {

        project = mock(Project.class);
        project.setCreatedBy(user);

        when(projectService.findById(1)).thenReturn(project);
        when(project.getCreatedBy()).thenReturn(user);

        mockMvc.perform(get("/dashboard/editProject")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/project-form"))
                .andExpect(model().attributeExists("formProject"));

        verify(project).getTitle();
        verify(project).getDescription();
        verify(project).getGuestUsers();
        verify(project).getCollaborators();
    }

    @Test
    public void getEditProjectFormIsAdmin() throws Exception {

        project = mock(Project.class);
        project.setCreatedBy(user);

        when(projectService.findById(1)).thenReturn(project);
        when(project.getCreatedBy()).thenReturn(user);

        mockMvc.perform(get("/dashboard/editProject")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/project-form"))
                .andExpect(model().attributeExists("formProject"));

        verify(project).getTitle();
        verify(project).getDescription();
        verify(project).getGuestUsers();
        verify(project).getCollaborators();
    }

    @Test
    public void getEditProjectFormIsNotOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);
        when(project.getCreatedBy()).thenReturn(user);

        mockMvc.perform(get("/dashboard/editProject")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("anotherUser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processProjectFormHasErrors() throws Exception {

        mockMvc.perform(post("/dashboard/processProject")
                        .param("title", "")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/project-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("formProject"));
    }

    @Test
    public void processProjectFormIsGuestUser() throws Exception {

        mockMvc.perform(post("/dashboard/processProject")
                        .param("title", "title")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER", "GUEST")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processProjectFormCreateNewProject() throws Exception {

        formProject = new FormProject();

        when(userService.findByUsername("user")).thenReturn(user);

        mockMvc.perform(post("/dashboard/processProject")
                        .param("title", "title")
                        .flashAttr("formProject", formProject)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));

        verify(projectService).createProject(formProject, user);
    }

    @Test
    public void processProjectFormUpdateProjectIsOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(post("/dashboard/processProject")
                        .param("id", "1")
                        .param("title", "title")
                        .flashAttr("formProject", formProject)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));

        verify(projectService).updateProject(formProject);
    }

    @Test
    public void processProjectFormUpdateProjectIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(post("/dashboard/processProject")
                        .param("id", "1")
                        .param("title", "title")
                        .flashAttr("formProject", formProject)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));

        verify(projectService).updateProject(formProject);
    }

    @Test
    public void processProjectFormUpdateProjectIsNotOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(post("/dashboard/processProject")
                        .param("id", "1")
                        .param("title", "title")
                        .flashAttr("formProject", formProject)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("anotherUser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void deleteProjectInvalidProjectId() throws Exception {

        when(projectService.findById(0)).thenReturn(null);

        mockMvc.perform(delete("/dashboard/deleteProject/{projectId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void deleteProjectIsOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(delete("/dashboard/deleteProject/{projectId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));

        verify(projectService).deleteById(1);
    }

    @Test
    public void deleteProjectIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(delete("/dashboard/deleteProject/{projectId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));

        verify(projectService).deleteById(1);
    }

    @Test
    public void deleteProjectIsNotOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(delete("/dashboard/deleteProject/{projectId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("anotherUser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }
}
