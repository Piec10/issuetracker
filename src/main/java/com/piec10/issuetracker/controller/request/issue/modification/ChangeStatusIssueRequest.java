package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class ChangeStatusIssueRequest extends IssueModificationRequest {

    private int statusId;

    public ChangeStatusIssueRequest(IssueService issueService, int issueId, HttpServletRequest request, int statusId) {
        super(issueService, issueId, request);
        this.statusId = statusId;
    }

    @Override
    public void doWork() {
        issueService.changeIssueStatus(issue, statusId);
    }
}
