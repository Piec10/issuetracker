package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
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
import java.util.Date;

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

    private static User owner;

    private static User collaborator;

    private static User guest;

    private static User notGuest;

    private static User admin;

    private static Issue issue;

    private static Issue closedIssue;

    private static FormIssue formIssue;

    @BeforeAll
    public static void beforeAll() {

        project = new Project();

        owner = new User();
        owner.setUsername("owner");
        collaborator = new User();
        collaborator.setUsername("collaborator");
        guest = new User();
        guest.setUsername("guest");
        notGuest = new User();
        notGuest.setUsername("notGuest");
        admin = new User();
        admin.setUsername("admin");

        issue = new Issue();
        issue.setCreatedBy(owner);
        issue.setProject(project);

        closedIssue = new Issue();
        closedIssue.setCreatedBy(owner);
        closedIssue.setProject(project);
        closedIssue.setClosedAt(new Date());


        project.setId(1);
        project.setCreatedBy(owner);
        project.setCollaborators(Arrays.asList(owner, collaborator));
        project.setGuestUsers(Arrays.asList(owner, collaborator, guest));

        formIssue = new FormIssue();
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

        when(userService.findByUsername("guest")).thenReturn(guest);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("guest").roles("USER")))
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

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("notGuest")).thenReturn(notGuest);

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notGuest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getIssueDetailsInvalidIssueId() throws Exception {

        when(issueService.findById(0)).thenReturn(null);

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsAdmin() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-details"))
                .andExpect(model().attributeExists("issue"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsProjectGuestUser() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("guest")).thenReturn(guest);

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("guest").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-details"))
                .andExpect(model().attributeExists("issue"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsNotProjectGuestUser() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("notGuest")).thenReturn(notGuest);

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notGuest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormInvalidProjectId() throws Exception {

        when(projectService.findById(0)).thenReturn(null);

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectCollaborator() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("collaborator")).thenReturn(collaborator);

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectGuestUser() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("guest")).thenReturn(guest);

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("guest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsNotProjectGuestUser() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("notGuest")).thenReturn(notGuest);

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notGuest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getEditIssueFormInvalidIssueId() throws Exception {

        when(issueService.findById(0)).thenReturn(null);

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getEditIssueFormValidIssueIdIsOwner() throws Exception {

        Issue mockIssue = mock(Issue.class);
        when(mockIssue.getCreatedBy()).thenReturn(owner);
        when(mockIssue.getProject()).thenReturn(project);

        when(issueService.findById(1)).thenReturn(mockIssue);

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"));

        verify(mockIssue).getSummary();
        verify(mockIssue).getDescription();
        verify(mockIssue).getPriority();
        verify(mockIssue).getProject();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsNotOwner() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(userService.findByUsername("notGuest")).thenReturn(notGuest);

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notGuest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getEditIssueFormValidIssueIdIsAdmin() throws Exception {

        Issue mockIssue = mock(Issue.class);
        when(mockIssue.getCreatedBy()).thenReturn(owner);
        when(mockIssue.getProject()).thenReturn(project);

        when(issueService.findById(1)).thenReturn(mockIssue);

        when(userService.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"));

        verify(mockIssue).getId();
        verify(mockIssue).getSummary();
        verify(mockIssue).getDescription();
        verify(mockIssue).getPriority();
        verify(mockIssue).getProject();
    }

    @Test
    public void processIssueFormHasErrors() throws Exception {

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("summary", "")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("formIssue"));
    }

    @Test
    public void processIssueFormInvalidProjectId() throws Exception {

        when(projectService.findById(0)).thenReturn(null);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("summary", "summary")
                        .param("projectId", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void processIssueFormNewIssueIsProjectCollaborator() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("collaborator")).thenReturn(collaborator);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).createIssue(formIssue, collaborator, project);
    }

    @Test
    public void processIssueFormNewIssueIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processIssueFormNewIssueIsProjectGuest() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("guest")).thenReturn(guest);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("guest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processIssueFormUpdateIssueInvalidIssueId() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("owner")).thenReturn(owner);

        when(issueService.findById(2)).thenReturn(null);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","2")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));
    }

    @Test
    public void processIssueFormUpdateIssueIsIssueOwner() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("owner")).thenReturn(owner);

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","1")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).updateIssue(formIssue);
    }

    @Test
    public void processIssueFormUpdateIssueIsProjectCollaborator() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("collaborator")).thenReturn(collaborator);

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","1")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processIssueFormUpdateIssueIsAdmin() throws Exception {

        when(projectService.findById(1)).thenReturn(project);

        when(userService.findByUsername("admin")).thenReturn(admin);

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","1")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER","ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).updateIssue(formIssue);
    }

    @Test
    public void deleteIssueInvalidIssueId() throws Exception {

        when(issueService.findById(0)).thenReturn(null);

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void deleteIssueIsOwner() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).deleteById(1);
    }

    @Test
    public void deleteIssueIsAdmin() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).deleteById(1);
    }

    @Test
    public void deleteIssueIsNotOwner() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("notOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void closeIssueInvalidIssueId() throws Exception {

        when(issueService.findById(0)).thenReturn(null);

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void closeIssueIsOwner() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).closeIssue(1,owner);
    }

    @Test
    public void closeIssueIsAdmin() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(userService.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).closeIssue(1,admin);
    }

    @Test
    public void closeIssueIsNotOwner() throws Exception {

        when(issueService.findById(1)).thenReturn(issue);

        when(userService.findByUsername("notGuest")).thenReturn(notGuest);

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("notGuest").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void closeIssueAlreadyClosed() throws Exception {

        when(issueService.findById(2)).thenReturn(closedIssue);

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "2")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));
    }
}
