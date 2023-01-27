package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.controller.util.MockIssueService;
import com.piec10.issuetracker.controller.util.MockProjectService;
import com.piec10.issuetracker.controller.util.MockUserService;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;

import static com.piec10.issuetracker.controller.util.MockRequestUsers.*;
import static org.mockito.Mockito.*;

@Import(SecurityConfig.class)
@WebMvcTest(IssueController.class)
public class IssueControllerTest extends BaseControllerTest {

    private static final String ISSUES_URL = "/dashboard/issues";
    private static final String ISSUE_DETAILS_URL = "/dashboard/issue";
    private static final String NEW_ISSUE_URL = "/dashboard/newIssue";
    private static final String EDIT_ISSUE_URL = "/dashboard/editIssue";
    private static final String PROCESS_ISSUE_URL = "/dashboard/processIssue";
    private static final String DELETE_ISSUE_URL = "/dashboard/deleteIssue/{issueId}";
    private static final String CLOSE_ISSUE_URL = "/dashboard/closeIssue/{issueId}";
    private static final String REOPEN_ISSUE_URL = "/dashboard/reopenIssue/{issueId}";
    private static final String CHANGE_ISSUE_STATUS_URL = "/dashboard/changeIssueStatus/{issueId}";
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
        givenUrl(ISSUES_URL);
        andUser(user());
        andParam("projectId", "0");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsAdmin() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(admin());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpectValidOpenIssuesView();
    }

    @Test
    public void getIssuesValidProjectIdOpenShowParamIsAdmin() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(admin());
        andParam("projectId", "1");
        andParam("show", "open");
        whenPerformGet();
        thenExpectValidOpenIssuesView();
    }

    @Test
    public void getIssuesValidProjectIdClosedShowParamIsAdmin() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(admin());
        andParam("projectId", "1");
        andParam("show", "closed");
        whenPerformGet();
        thenExpectValidIssuesView();
        andExpectMethodCalledOnceIn(issueService).findClosed(1);
    }


    @Test
    public void getIssuesValidProjectIdAllShowParamIsAdmin() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(admin());
        andParam("projectId", "1");
        andParam("show", "all");
        whenPerformGet();
        thenExpectValidIssuesView();
        andExpectMethodCalledOnceIn(issueService).findAll(1);
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsProjectFollower() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpectValidOpenIssuesView();
    }

    @Test
    public void getIssuesValidProjectIdDefaultShowParamIsNotProjectFollower() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(notFollower());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getIssuesSortByPriorityAscDefaultShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("sort", "priorityAsc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findOpenPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescDefaultShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("sort", "priorityDesc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findOpenPriorityDesc(1);
    }

    @Test
    public void getIssuesSortByPriorityAscClosedShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("show", "closed");
        andParam("sort", "priorityAsc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findClosedPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescClosedShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("show", "closed");
        andParam("sort", "priorityDesc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findClosedPriorityDesc(1);
    }

    @Test
    public void getIssuesSortByPriorityAscAllShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("show", "all");
        andParam("sort", "priorityAsc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findAllPriorityAsc(1);
    }

    @Test
    public void getIssuesSortByPriorityDescAllShowParam() throws Exception {
        givenUrl(ISSUES_URL);
        andUser(follower());
        andParam("projectId", "1");
        andParam("show", "all");
        andParam("sort", "priorityDesc");
        whenPerformGet();
        thenExpectValidSortedIssuesView();
        andExpectMethodCalledOnceIn(issueService).findAllPriorityDesc(1);
    }

    @Test
    public void getIssueDetailsInvalidIssueId() throws Exception {
        givenUrl(ISSUE_DETAILS_URL);
        andUser(admin());
        andParam("issueId", "0");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getIssueDetailsInvalidProjectId() throws Exception {
        givenUrl(ISSUE_DETAILS_URL);
        andUser(admin());
        andParam("issueId", "4");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getIssueDetailsValidIssueIdIsAdmin() throws Exception {
        givenUrl(ISSUE_DETAILS_URL);
        andUser(admin());
        andParam("issueId", "1");
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/issue-details");
        andExpectModelAttribute("issue");
    }

    @Test
    public void getIssueDetailsValidIssueIdIsProjectFollower() throws Exception {
        givenUrl(ISSUE_DETAILS_URL);
        andUser(follower());
        andParam("issueId", "1");
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/issue-details");
        andExpectModelAttribute("issue");
    }

    @Test
    public void getIssueDetailsValidIssueIdIsNotProjectFollower() throws Exception {
        givenUrl(ISSUE_DETAILS_URL);
        andUser(notFollower());
        andParam("issueId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getNewIssueFormInvalidProjectId() throws Exception {
        givenUrl(NEW_ISSUE_URL);
        andUser(user());
        andParam("projectId", "0");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectCollaborator() throws Exception {
        givenUrl(NEW_ISSUE_URL);
        andUser(collaborator());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpectValidIssueFormView();
    }

    @Test
    public void getNewIssueFormValidProjectIdIsProjectFollower() throws Exception {
        givenUrl(NEW_ISSUE_URL);
        andUser(follower());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getNewIssueFormValidProjectIdIsNotProjectFollower() throws Exception {
        givenUrl(NEW_ISSUE_URL);
        andUser(notFollower());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getNewIssueFormValidProjectIdIsAdmin() throws Exception {
        givenUrl(NEW_ISSUE_URL);
        andUser(admin());
        andParam("projectId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getEditIssueFormInvalidIssueId() throws Exception {
        givenUrl(EDIT_ISSUE_URL);
        andUser(user());
        andParam("issueId", "0");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getEditIssueFormValidIssueIdIsOwner() throws Exception {
        givenUrl(EDIT_ISSUE_URL);
        andUser(owner());
        andParam("issueId", "2");
        whenPerformGet();
        thenExpectValidIssueFormView();
        andExpectEditIssueFormFilledCorrectly();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsNotOwner() throws Exception {
        givenUrl(EDIT_ISSUE_URL);
        andUser(user());
        andParam("issueId", "1");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsAdmin() throws Exception {
        givenUrl(EDIT_ISSUE_URL);
        andUser(admin());
        andParam("issueId", "2");
        whenPerformGet();
        thenExpectValidIssueFormView();
        andExpectEditIssueFormFilledCorrectly();
    }

    @Test
    public void getEditIssueFormValidIssueIdIsProjectOwner() throws Exception {
        givenUrl(EDIT_ISSUE_URL);
        andUser(projectOwner());
        andParam("issueId", "2");
        whenPerformGet();
        thenExpectValidIssueFormView();
        andExpectEditIssueFormFilledCorrectly();
    }

    @Test
    public void processIssueFormHasErrors() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(owner());
        andParam("summary", "");
        whenPerformPost();
        thenExpectIsOkAndView("dashboard/issue-form");
        andExpectModelErrors();
        andExpectModelAttributeHasErrors("formIssue");
        andExpectModelAttribute("allIssueTypes");
        andExpectModelAttribute("allIssueStatuses");
        andExpectMethodCalledOnceIn(issueService).findAllIssueTypes();
        andExpectMethodCalledOnceIn(issueService).findAllIssueStatuses();
    }

    @Test
    public void processIssueFormInvalidProjectId() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(owner());
        andParam("summary", "summary");
        andParam("projectId", "0");
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void processIssueFormNewIssueIsProjectCollaborator() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(collaborator());
        andParam("id", "0");
        andParam("summary", "summary");
        andParam("projectId", "1");
        andAttribute("formIssue", formIssue);
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).createIssue(formIssue,
                MockUserService.getCollaborator(), MockProjectService.getProject());
    }

    @Test
    public void processIssueFormNewIssueIsAdmin() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(admin());
        andParam("id", "0");
        andParam("summary", "summary");
        andParam("projectId", "1");
        whenPerformPost();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void processIssueFormNewIssueIsProjectFollower() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(follower());
        andParam("id", "0");
        andParam("summary", "summary");
        andParam("projectId", "1");
        whenPerformPost();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void processIssueFormNewIssueIsGuestUser() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(guest());
        andParam("id", "0");
        andParam("summary", "summary");
        andParam("projectId", "1");
        whenPerformPost();
        thenExpectIsOkAndView("dashboard/issue-form");
        andExpectModelAttribute("guestUserError");
    }

    @Test
    public void processIssueFormUpdateIssueInvalidIssueId() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(owner());
        andParam("id", "-1");
        andParam("summary", "summary");
        andParam("projectId", "1");
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
    }

    @Test
    public void processIssueFormUpdateIssueIsIssueOwner() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(owner());
        andParam("id", "1");
        andParam("summary", "summary");
        andParam("projectId", "1");
        andAttribute("formIssue", formIssue);
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).updateIssue(formIssue);
    }

    @Test
    public void processIssueFormUpdateIssueIsProjectCollaborator() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(collaborator());
        andParam("id", "1");
        andParam("summary", "summary");
        andParam("projectId", "1");
        whenPerformPost();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void processIssueFormUpdateIssueIsAdmin() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(admin());
        andParam("id", "1");
        andParam("summary", "summary");
        andParam("projectId", "1");
        andAttribute("formIssue", formIssue);
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).updateIssue(formIssue);
    }

    @Test
    public void processIssueFormUpdateIssueIsProjectOwner() throws Exception {
        givenUrl(PROCESS_ISSUE_URL);
        andUser(projectOwner());
        andParam("id", "1");
        andParam("summary", "summary");
        andParam("projectId", "1");
        andAttribute("formIssue", formIssue);
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).updateIssue(formIssue);
    }

    @Test
    public void deleteIssueInvalidIssueId() throws Exception {
        givenUrl(DELETE_ISSUE_URL,"0");
        andUser(user());
        whenPerformDelete();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void deleteIssueIsOwner() throws Exception {
        givenUrl(DELETE_ISSUE_URL,"1");
        andUser(owner());
        whenPerformDelete();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).deleteIssue(MockIssueService.getIssue());
    }

    @Test
    public void deleteIssueIsAdmin() throws Exception {
        givenUrl(DELETE_ISSUE_URL,"1");
        andUser(admin());
        whenPerformDelete();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).deleteIssue(MockIssueService.getIssue());
    }

    @Test
    public void deleteIssueIsProjectOwner() throws Exception {
        givenUrl(DELETE_ISSUE_URL,"1");
        andUser(projectOwner());
        whenPerformDelete();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).deleteIssue(MockIssueService.getIssue());
    }

    @Test
    public void deleteIssueIsNotOwner() throws Exception {
        givenUrl(DELETE_ISSUE_URL,"1");
        andUser(user());
        whenPerformDelete();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void closeIssueInvalidIssueId() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"0");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void closeIssueIsOwner() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"1");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).closeIssue(MockIssueService.getIssue(), MockUserService.getOwner());
    }

    @Test
    public void closeIssueIsAdmin() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"1");
        andUser(admin());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).closeIssue(MockIssueService.getIssue(), MockUserService.getAdmin());
    }

    @Test
    public void closeIssueIsProjectOwner() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"1");
        andUser(projectOwner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).closeIssue(MockIssueService.getIssue(), MockUserService.getProjectOwner());
    }

    @Test
    public void closeIssueIsNotOwner() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"1");
        andUser(notFollower());
        whenPerformPatch();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void closeIssueAlreadyClosed() throws Exception {
        givenUrl(CLOSE_ISSUE_URL,"3");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
    }

    @Test
    public void reopenIssueInvalidIssueId() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"0");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void reopenIssueIsOwner() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"3");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).reopenIssue(MockIssueService.getClosedIssue());
    }

    @Test
    public void reopenIssueIsAdmin() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"3");
        andUser(admin());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).reopenIssue(MockIssueService.getClosedIssue());
    }

    @Test
    public void reopenIssueIsProjectOwner() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"3");
        andUser(projectOwner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).reopenIssue(MockIssueService.getClosedIssue());
    }

    @Test
    public void reopenIssueIsNotOwner() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"3");
        andUser(notFollower());
        whenPerformPatch();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void reopenIssueAlreadyOpen() throws Exception {
        givenUrl(REOPEN_ISSUE_URL,"1");
        andUser(owner());
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
    }
    
    @Test
    public void changeIssueStatusIsOwner() throws Exception {
        givenUrl(CHANGE_ISSUE_STATUS_URL, "1");
        andUser(owner());
        andParam("statusId", "1");
        whenPerformPatch();
        thenExpect3xxRedirectionTo("/dashboard/issues?projectId=1");
        andExpectMethodCalledOnceIn(issueService).changeIssueStatus(MockIssueService.getIssue(),1);
    }

    private void thenExpectValidOpenIssuesView() throws Exception {
        thenExpectValidIssuesView();
        andExpectMethodCalledOnceIn(issueService).findOpen(1);
    }

    private void thenExpectValidSortedIssuesView() throws Exception {
        thenExpectValidIssuesView();
        andExpectModelAttribute("sort");
    }

    private void thenExpectValidIssuesView() throws Exception {
        thenExpectIsOkAndView("dashboard/issues");
        andExpectModelAttribute("issues");
        andExpectModelAttribute("show");
        andExpectModelAttribute("openIssuesCount");
        andExpectModelAttribute("closedIssuesCount");
        andExpectModelAttribute("project");
        andExpectModelAttribute("projectRoles");
        andExpectMethodCalledOnceIn(issueService).getOpenIssuesCount(1);
        andExpectMethodCalledOnceIn(issueService).getClosedIssuesCount(1);
    }

    private void thenExpectValidIssueFormView() throws Exception {
        thenExpectIsOkAndView("dashboard/issue-form");
        andExpectModelAttribute("formIssue");
        andExpectModelAttribute("allIssueTypes");
        andExpectModelAttribute("allIssueStatuses");
        andExpectMethodCalledOnceIn(issueService).findAllIssueTypes();
        andExpectMethodCalledOnceIn(issueService).findAllIssueStatuses();
    }

    private static void andExpectEditIssueFormFilledCorrectly() {
        verify(MockIssueService.getMockIssue()).getId();
        verify(MockIssueService.getMockIssue()).getSummary();
        verify(MockIssueService.getMockIssue()).getDescription();
        verify(MockIssueService.getMockIssue()).getPriority();
        verify(MockIssueService.getMockIssue(), times(2)).getProject();
        verify(MockIssueService.getMockIssue()).getIssueTags();
        verify(MockIssueService.getMockIssue()).getIssueType();
        verify(MockIssueService.getMockIssue()).getIssueStatus();
    }
}
