package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.controller.util.MockIssueService;
import com.piec10.issuetracker.controller.util.MockProjectService;
import com.piec10.issuetracker.controller.util.MockUserService;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(IssueController.class)
public class IssueControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private IssueService issueService;
    @MockBean
    private ProjectService projectService;

    private static FormIssue formIssue;

    @PostConstruct
    public void setup() {
        MockUserService.mockSetup(userService);
        MockIssueService.mockSetup(issueService);
        MockProjectService.mockSetup(projectService);

        MockIssueService.getIssue().setCreatedBy(MockUserService.getOwner());
        MockIssueService.getIssue().setProject(MockProjectService.getProject());
        MockIssueService.getClosedIssue().setCreatedBy(MockUserService.getOwner());
        MockIssueService.getClosedIssue().setProject(MockProjectService.getProject());
        MockIssueService.getClosedIssue().setClosedAt(new Date());

        when(MockIssueService.getMockIssue().getCreatedBy()).thenReturn(MockUserService.getOwner());
        when(MockIssueService.getMockIssue().getProject()).thenReturn(MockProjectService.getProject());

        MockProjectService.getProject().setCreatedBy(MockUserService.getProjectOwner());
        MockProjectService.getProject().setCollaborators(Arrays.asList(
                MockUserService.getOwner(),
                MockUserService.getCollaborator()
        ));
        MockProjectService.getProject().setFollowers(Arrays.asList(
                MockUserService.getOwner(),
                MockUserService.getCollaborator(),
                MockUserService.getFollower()
        ));

        formIssue = new FormIssue();
    }

    @Test
    public void getIssuesInvalidProjectId() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsAdmin() throws Exception {

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
    public void getIssuesValidProjectIdDefaultShowParamIsProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
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
    public void getIssuesValidProjectIdDefaultShowParamIsNotProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getIssuesSortByPriorityAscDefaultShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("sort","priorityAsc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findOpenPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescDefaultShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("sort","priorityDesc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findOpenPriorityDesc(1);
    }

    @Test
    public void getIssuesSortByPriorityAscClosedShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show", "closed")
                        .param("sort","priorityAsc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findClosedPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescClosedShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show", "closed")
                        .param("sort","priorityDesc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findClosedPriorityDesc(1);
    }

    @Test
    public void getIssuesSortByPriorityAscAllShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show", "all")
                        .param("sort","priorityAsc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findAllPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescAllShowParam() throws Exception {

        mockMvc.perform(get("/dashboard/issues")
                        .param("projectId","1")
                        .param("show", "all")
                        .param("sort","priorityDesc")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issues"))
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("openIssuesCount"))
                .andExpect(model().attributeExists("closedIssuesCount"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("projectRoles"));

        verify(issueService).getOpenIssuesCount(1);
        verify(issueService).getClosedIssuesCount(1);
        verify(issueService).findAllPriorityDesc(1);
    }

    @Test
    public void getIssueDetailsInvalidIssueId() throws Exception {

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getIssueDetailsInvalidProjectId() throws Exception {

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","4")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsAdmin() throws Exception {

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-details"))
                .andExpect(model().attributeExists("issue"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-details"))
                .andExpect(model().attributeExists("issue"));
    }

    @Test
    public void getIssueDetailsValidIssueIdIsNotProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/issue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormInvalidProjectId() throws Exception {

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectCollaborator() throws Exception {

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"))
                .andExpect(model().attributeExists("allIssueTypes"))
                .andExpect(model().attributeExists("allIssueStatuses"));

        verify(issueService).findAllIssueTypes();
        verify(issueService).findAllIssueStatuses();
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsNotProjectFollower() throws Exception {

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getNewIssueFormValidProjectIdIsAdmin() throws Exception {

        mockMvc.perform(get("/dashboard/newIssue")
                        .param("projectId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getEditIssueFormInvalidIssueId() throws Exception {

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","0")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getEditIssueFormValidIssueIdIsOwner() throws Exception {

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","2")
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"))
                .andExpect(model().attributeExists("allIssueTypes"))
                .andExpect(model().attributeExists("allIssueStatuses"));

        verify(issueService).findAllIssueTypes();
        verify(issueService).findAllIssueStatuses();

        verify(MockIssueService.getMockIssue()).getSummary();
        verify(MockIssueService.getMockIssue()).getDescription();
        verify(MockIssueService.getMockIssue()).getPriority();
        verify(MockIssueService.getMockIssue()).getProject();
        verify(MockIssueService.getMockIssue()).getIssueTags();
        verify(MockIssueService.getMockIssue()).getIssueType();
        verify(MockIssueService.getMockIssue()).getIssueStatus();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsNotOwner() throws Exception {

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","1")
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getEditIssueFormValidIssueIdIsAdmin() throws Exception {

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","2")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"))
                .andExpect(model().attributeExists("allIssueTypes"));

        verify(issueService).findAllIssueTypes();

        verify(MockIssueService.getMockIssue()).getId();
        verify(MockIssueService.getMockIssue()).getSummary();
        verify(MockIssueService.getMockIssue()).getDescription();
        verify(MockIssueService.getMockIssue()).getPriority();
        verify(MockIssueService.getMockIssue()).getProject();
        verify(MockIssueService.getMockIssue()).getIssueTags();
        verify(MockIssueService.getMockIssue()).getIssueType();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsProjectOwner() throws Exception {

        mockMvc.perform(get("/dashboard/editIssue")
                        .param("issueId","2")
                        .with(SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("formIssue"))
                .andExpect(model().attributeExists("allIssueTypes"))
                .andExpect(model().attributeExists("allIssueStatuses"));

        verify(issueService).findAllIssueTypes();
        verify(issueService).findAllIssueStatuses();

        verify(MockIssueService.getMockIssue()).getId();
        verify(MockIssueService.getMockIssue()).getSummary();
        verify(MockIssueService.getMockIssue()).getDescription();
        verify(MockIssueService.getMockIssue()).getPriority();
        verify(MockIssueService.getMockIssue(), times(2)).getProject();
        verify(MockIssueService.getMockIssue()).getIssueTags();
        verify(MockIssueService.getMockIssue()).getIssueType();
        verify(MockIssueService.getMockIssue()).getIssueStatus();
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
                .andExpect(model().attributeHasErrors("formIssue"))
                .andExpect(model().attributeExists("allIssueTypes"))
                .andExpect(model().attributeExists("allIssueStatuses"));

        verify(issueService).findAllIssueTypes();
        verify(issueService).findAllIssueStatuses();
    }

    @Test
    public void processIssueFormInvalidProjectId() throws Exception {

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

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).createIssue(formIssue, MockUserService.getCollaborator(), MockProjectService.getProject());
    }

    @Test
    public void processIssueFormNewIssueIsAdmin() throws Exception {

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
    public void processIssueFormNewIssueIsProjectFollower() throws Exception {

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("follower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processIssueFormNewIssueIsGuestUser() throws Exception {

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER", "GUEST")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/issue-form"))
                .andExpect(model().attributeExists("guestUserError"));
    }

    @Test
    public void processIssueFormUpdateIssueInvalidIssueId() throws Exception {

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","0")
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
    public void processIssueFormUpdateIssueIsProjectOwner() throws Exception {

        mockMvc.perform(post("/dashboard/processIssue")
                        .param("id","1")
                        .param("summary", "summary")
                        .param("projectId", "1")
                        .flashAttr("formIssue", formIssue)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).updateIssue(formIssue);
    }

    @Test
    public void deleteIssueInvalidIssueId() throws Exception {

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void deleteIssueIsOwner() throws Exception {

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).deleteById(1);
    }

    @Test
    public void deleteIssueIsAdmin() throws Exception {

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).deleteById(1);
    }

    @Test
    public void deleteIssueIsProjectOwner() throws Exception {

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).deleteById(1);
    }

    @Test
    public void deleteIssueIsNotOwner() throws Exception {

        mockMvc.perform(delete("/dashboard/deleteIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("notOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void closeIssueInvalidIssueId() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void closeIssueIsOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

//        verify(issueService).closeIssue(1,getOwner());
    }

    @Test
    public void closeIssueIsAdmin() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).closeIssue(1,MockUserService.getAdmin());
    }

    @Test
    public void closeIssueIsProjectOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).closeIssue(1, MockUserService.getProjectOwner());
    }

    @Test
    public void closeIssueIsNotOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void closeIssueAlreadyClosed() throws Exception {

        mockMvc.perform(patch("/dashboard/closeIssue/{issueId}", "3")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));
    }

    @Test
    public void reopenIssueInvalidIssueId() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "0")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void reopenIssueIsOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "3")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).reopenIssue(3);
    }

    @Test
    public void reopenIssueIsAdmin() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "3")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER","ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).reopenIssue(3);
    }

    @Test
    public void reopenIssueIsProjectOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "3")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));

        verify(issueService).reopenIssue(3);
    }

    @Test
    public void reopenIssueIsNotOwner() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "3")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void reopenIssueAlreadyOpen() throws Exception {

        mockMvc.perform(patch("/dashboard/reopenIssue/{issueId}", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/issues?projectId=1"));
    }
}
