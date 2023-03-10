package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class DeleteIssueRequest extends IssueModificationRequest {

    public DeleteIssueRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        super(issueService, issueId, request);
    }

    @Override
    public void doWork() {
        issueService.deleteIssue(issue);
    }
}
