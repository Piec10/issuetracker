package com.piec10.issuetracker;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IssueServiceTest {

    @MockBean
    IssueRepository issueRepository;

    @Autowired
    IssueService issueService;

    @Test
    public void findAllService() {

        Issue issue1 = new Issue();
        Issue issue2 = new Issue();
        Issue issue3 = new Issue();

        List<Issue> issues = new ArrayList<>(Arrays.asList(issue1,issue2,issue3));

        when(issueRepository.findAll()).thenReturn(issues);

        assertEquals(3,issueService.findAll().size(),"Should return list with 3 issues");

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

        FormIssue formIssue = new FormIssue();
        formIssue.setSummary("summary");
        formIssue.setDescription("description");

        User user = new User();
        user.setUsername("user");

        ArgumentCaptor<Issue> capturedIssue = ArgumentCaptor.forClass(Issue.class);

        issueService.createIssue(formIssue, user);

        verify(issueRepository).save(capturedIssue.capture());

        assertEquals(formIssue.getId(), capturedIssue.getValue().getId(), "Issue id should match and be 0 for new issue");
        assertEquals(formIssue.getSummary(), capturedIssue.getValue().getSummary(), "Issue summary should match");
        assertEquals(formIssue.getDescription(), capturedIssue.getValue().getDescription(), "Issue description should match");
        assertEquals(formIssue.getPriority(), capturedIssue.getValue().getPriority(), "Issue priority should match");

        assertEquals(user.getUsername(), capturedIssue.getValue().getCreatedBy().getUsername(), "Issue creator name should match");

        assertNotNull(capturedIssue.getValue().getCreatedAt(), "Issue creation date should not be null");
        assertTrue(new Date().getTime() > capturedIssue.getValue().getCreatedAt().getTime(), "Issue creation date should be older than current date");

        assertNull(capturedIssue.getValue().getClosedBy(), "Issue closed by should be null for new issue");
        assertNull(capturedIssue.getValue().getClosedAt(), "Issue close date should be null for new issue");

    }

    @Test
    public void updateValidIssueService() {


    }

}
