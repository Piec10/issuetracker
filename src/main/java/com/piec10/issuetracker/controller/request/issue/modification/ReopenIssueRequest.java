package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class ReopenIssueRequest extends IssueModificationRequest {


    public ReopenIssueRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        super(issueService, issueId, request);
    }

    @Override
    public void doWork() {
        issueService.reopenIssue(issue);
    }
}
