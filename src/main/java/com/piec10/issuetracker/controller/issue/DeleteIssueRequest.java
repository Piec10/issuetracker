package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class DeleteIssueRequest extends IssueRequest {

    private IssueService issueService;

    public DeleteIssueRequest(IssueService issueService, Issue issue, HttpServletRequest request) {
        super(issue, request);
        this.issueService = issueService;
    }

    @Override
    public void modifyIssue() {
        issueService.deleteIssue(issue);
    }
}
