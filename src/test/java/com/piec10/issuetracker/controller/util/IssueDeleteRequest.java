package com.piec10.issuetracker.controller.util;

import com.piec10.issuetracker.controller.IssueRequest;

import javax.servlet.http.HttpServletRequest;

public class IssueDeleteRequest extends IssueRequest {

    public IssueDeleteRequest(int issueId, HttpServletRequest request) {
        super(issueId, request);
    }

    @Override
    public void modifyIssue() {
        issueService.deleteById(issueId);
    }
}
