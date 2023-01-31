package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class CloseIssueRequest extends IssueRequest {

    private IssueService issueService;
    private User closedBy;

    public CloseIssueRequest(IssueService issueService, Issue issue, HttpServletRequest request, User closedBy) {
        super(issue, request);
        this.issueService = issueService;
        this.closedBy = closedBy;
    }

    @Override
    public void modifyIssue() {
        issueService.closeIssue(issue, closedBy);
    }
}
