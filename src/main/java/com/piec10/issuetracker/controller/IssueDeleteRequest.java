package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class IssueDeleteRequest extends IssueRequest {


    public IssueDeleteRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        super(issueService, issueId, request);
    }

    @Override
    public void modifyIssue() {
        issueService.deleteById(issueId);
    }
}
