package com.piec10.issuetracker.controller;

import javax.servlet.http.HttpServletRequest;

public class IssueDeleteRequest extends IssueRequestImpl {

    public IssueDeleteRequest(int issueId, HttpServletRequest request) {
        super(issueId, request);
    }

    @Override
    public void modifyIssue() {
        issueService.deleteById(issueId);
    }
}
