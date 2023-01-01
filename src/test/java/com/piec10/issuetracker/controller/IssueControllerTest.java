package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;
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

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(IssueController.class)
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueService issueService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectService projectService;

    private static Project project;

    private static User user;

    @BeforeAll
    public static void beforeAll() {

        project = new Project();
        user = new User();
        user.setUsername("user");

        project.setCreatedBy(user);
        project.setCollaborators(Arrays.asList(user));
        project.setGuestUsers(Arrays.asList(user));
    }

    @Test
    public void getIssuesInvalidProjectId() throws Exception {

        when(projectService.findById(0)).thenReturn(null);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findOpen(1);
    }

    @Test
    public void getIssuesValidProjectIdOpenShowParamIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show","open")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findOpen(1);
    }

    @Test
    public void getIssuesValidProjectIdClosedShowParamIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show","closed")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findClosed(1);
    }

    @Test
    public void getIssuesValidProjectIdAllShowParamIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show","all")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findAll(1);
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsProjectGuestUser() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("user")).thenReturn(user);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findOpen(1);
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsNotProjectGuestUser() throws Exception {

        User user2 = new User();
        user2.setUsername("user2");

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("user2")).thenReturn(user2);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("user2").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }
}
