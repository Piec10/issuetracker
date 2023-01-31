package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class IssueDeleteRequest extends IssueRequest {

    private IssueService issueService;

    public IssueDeleteRequest(IssueService issueService, Issue issue, HttpServletRequest request) {
        super(issue, request);
        this.issueService = issueService;
    }

    @Override
    public void modifyIssue() {
        issueService.deleteIssue(issue);
    }
}
