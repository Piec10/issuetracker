package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.IssueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueService issueService = new IssueServiceImpl();

    @Captor
    private ArgumentCaptor<Issue> capturedIssue;

    private FormIssue formIssue;

    private Issue issue;

    private User createdBy;

    private User closedBy;

    private Date createdAt;

    private Date closedAt;

    private Project project;

    @BeforeEach
    public void beforeEach() {

        formIssue = new FormIssue();
        formIssue.setSummary("new summary");
        formIssue.setDescription("new description");

        createdBy = new User();
        createdBy.setUsername("user");

        closedBy = new User();
        closedBy.setUsername("user2");

        createdAt = new Date(new Date().getTime() - 1000);
        closedAt = new Date();

        issue = new Issue();
        issue.setSummary("summary");
        issue.setDescription("description");
        issue.setPriority(0);
        issue.setCreatedBy(createdBy);
        issue.setCreatedAt(createdAt);

        project = new Project();
        project.setId(1);
        project.setTitle("title");
        project.setDescription("description");

    }

    @Test
    public void findAllService() {

        Issue issue1 = new Issue();
        Issue issue2 = new Issue();
        Issue issue3 = new Issue();

        List<Issue> issues = new ArrayList<>(Arrays.asList(issue1,issue2,issue3));

        when(issueRepository.findAll(1)).thenReturn(issues);

        assertEquals(3,issueService.findAll(1).size(),"Should return list with 3 issues");

    }

    @Test
    public void findByIdServiceValidId() {

        Issue issue = new Issue();
        issue.setId(1);
        when(issueRepository.findById(1)).thenReturn(Optional.of(issue));

        assertEquals(1, issueService.findById(1).getId(), "Should return issue with id=1");
    }

    @Test
    public void findByIdServiceInvalidId() {

        when(issueRepository.findById(0)).thenReturn(Optional.empty());

        assertNull(issueService.findById(0), "There should be no issue with id=0");
    }

    @Test
    public void createNewValidIssueService() {

        issueService.createIssue(formIssue, createdBy, project);

        verify(issueRepository).save(capturedIssue.capture());

        assertEquals(formIssue.getId(), capturedIssue.getValue().getId(), "Issue id should match and be 0 for new issue");
        assertEquals(formIssue.getSummary(), capturedIssue.getValue().getSummary(), "Issue summary should match");
        assertEquals(formIssue.getDescription(), capturedIssue.getValue().getDescription(), "Issue description should match");
        assertEquals(formIssue.getPriority(), capturedIssue.getValue().getPriority(), "Issue priority should match");

        assertEquals(createdBy.getUsername(), capturedIssue.getValue().getCreatedBy().getUsername(), "Issue creator name should match");

        assertNotNull(capturedIssue.getValue().getCreatedAt(), "Issue creation date should not be null");
        assertTrue(new Date().getTime() > capturedIssue.getValue().getCreatedAt().getTime(), "Issue creation date should be older than current date");

        assertNull(capturedIssue.getValue().getClosedBy(), "Issue closed by should be null for new issue");
        assertNull(capturedIssue.getValue().getClosedAt(), "Issue close date should be null for new issue");

        assertEquals(project.getId(), capturedIssue.getValue().getProject().getId(), "Project id should match");
    }

    @Test
    public void updateValidIssueService() {

        int id = 2;
        formIssue.setId(id);

        issue.setId(id);

        issue.setClosedBy(closedBy);
        issue.setClosedAt(closedAt);

        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));

        issueService.updateIssue(formIssue);

        verify(issueRepository).save(capturedIssue.capture());

        assertEquals(formIssue.getId(), capturedIssue.getValue().getId(), "Issue id should match");
        assertEquals(formIssue.getSummary(), capturedIssue.getValue().getSummary(), "Issue summary should be updated");
        assertEquals(formIssue.getDescription(), capturedIssue.getValue().getDescription(), "Issue description should be updated");
        assertEquals(formIssue.getPriority(), capturedIssue.getValue().getPriority(), "Issue priority should be updated");

        assertEquals(createdBy.getUsername(), capturedIssue.getValue().getCreatedBy().getUsername(), "Issue creator should not change");
        assertEquals(createdAt, capturedIssue.getValue().getCreatedAt(), "Issue creation date should not change");

        assertEquals(closedBy.getUsername(), capturedIssue.getValue().getClosedBy().getUsername(), "Issue closedBy user should not change");
        assertEquals(closedAt, capturedIssue.getValue().getClosedAt(), "Issue close date should not change");
    }

    @Test
    public void closeIssueService() {

        int id = 4;

        issue.setId(id);

        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));

        issueService.closeIssue(id, closedBy);

        verify(issueRepository).save(capturedIssue.capture());

        assertEquals(issue.getId(), capturedIssue.getValue().getId(), "Issue id should not change");
        assertEquals(issue.getSummary(), capturedIssue.getValue().getSummary(), "Issue summary should not change");
        assertEquals(issue.getDescription(), capturedIssue.getValue().getDescription(), "Issue description should not change");
        assertEquals(issue.getPriority(), capturedIssue.getValue().getPriority(), "Issue priority should not change");

        assertEquals(issue.getCreatedBy().getUsername(), capturedIssue.getValue().getCreatedBy().getUsername(), "Issue creator should not change");
        assertEquals(issue.getCreatedAt(), capturedIssue.getValue().getCreatedAt(), "Issue creation date should not change");

        assertEquals(closedBy.getUsername(), capturedIssue.getValue().getClosedBy().getUsername(), "Issue closedBy user should match");
        assertNotNull(capturedIssue.getValue().getClosedAt(), "Issue close date should not be null");

        assertTrue(issue.getCreatedAt().getTime() < capturedIssue.getValue().getClosedAt().getTime(), "Issue close date should be older than create date");

    }

    @Test
    public void reopenIssueService() {

        int id = 4;

        issue.setId(id);

        issue.setClosedBy(closedBy);
        issue.setClosedAt(closedAt);

        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));

        issueService.reopenIssue(id);

        verify(issueRepository).save(capturedIssue.capture());

        assertEquals(issue.getId(), capturedIssue.getValue().getId(), "Issue id should not change");
        assertEquals(issue.getSummary(), capturedIssue.getValue().getSummary(), "Issue summary should not change");
        assertEquals(issue.getDescription(), capturedIssue.getValue().getDescription(), "Issue description should not change");
        assertEquals(issue.getPriority(), capturedIssue.getValue().getPriority(), "Issue priority should not change");

        assertEquals(issue.getCreatedBy().getUsername(), capturedIssue.getValue().getCreatedBy().getUsername(), "Issue creator should not change");
        assertEquals(issue.getCreatedAt(), capturedIssue.getValue().getCreatedAt(), "Issue creation date should not change");

        assertNull(capturedIssue.getValue().getClosedBy(), "Issue closedBy should be null");
        assertNull(capturedIssue.getValue().getClosedAt(), "Issue close date should be null");

    }

}
