package com.piec10.issuetracker.controller.util;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class MockIssueService {

    private static Issue issue = new Issue();
    private static Issue closedIssue = new Issue();
    static int mockIssueId;
    private static Issue mockIssue = mock(Issue.class);
    private static Issue issueInvalidProject = new Issue();

    public static void mockSetup(IssueService issueService) {
        issue.setId(1);
        mockIssueId = 2;
        closedIssue.setId(3);
        issueInvalidProject.setId(4);

        when(issueService.findById(0)).thenReturn(null);
        when(issueService.findById(issue.getId())).thenReturn(issue);
        when(issueService.findById(mockIssueId)).then(a -> resetMock());
        when(issueService.findById(mockIssueId)).thenReturn(mockIssue);
        when(issueService.findById(closedIssue.getId())).thenReturn(closedIssue);
        when(issueService.findById(issueInvalidProject.getId())).thenReturn(issueInvalidProject);
    }

    public static Issue getIssue() {
        return issue;
    }

    public static Issue getClosedIssue() {
        return closedIssue;
    }

    public static Issue getMockIssue() {
        return mockIssue;
    }

    private static Issue resetMock() {
        mockIssue = mock(Issue.class);
        return mockIssue;
    }
}
