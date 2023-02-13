package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class ChangeTypeIssueRequest extends IssueModificationRequest {

    private int typeId;

    public ChangeTypeIssueRequest(IssueService issueService, int issueId, HttpServletRequest request, int typeId) {
        super(issueService, issueId, request);
        this.typeId = typeId;
    }

    @Override
    public void doWork() {
        issueService.changeIssueType(issue, typeId);
    }
}
