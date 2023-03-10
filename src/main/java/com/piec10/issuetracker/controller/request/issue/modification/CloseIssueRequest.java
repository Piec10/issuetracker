package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class CloseIssueRequest extends IssueModificationRequest {

    private User closedBy;

    public CloseIssueRequest(IssueService issueService, int issueId, HttpServletRequest request, User closedBy) {
        super(issueService, issueId, request);
        this.closedBy = closedBy;
    }

    @Override
    public void doWork() {
        issueService.closeIssue(issue, closedBy);
    }
}
