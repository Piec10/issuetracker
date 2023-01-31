package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class DeleteIssueRequest extends IssueRequest {

    public DeleteIssueRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        super(issueService, issueId, request);
    }

    @Override
    public void modifyIssue() {
        issueService.deleteIssue(issue);
    }
}
