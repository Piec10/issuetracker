package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

public class UpdateIssueRequest extends IssueModificationRequest {

    private FormIssue formIssue;

    public UpdateIssueRequest(IssueService issueService, int issueId, HttpServletRequest request, FormIssue formIssue) {
        super(issueService, issueId, request);
        this.formIssue = formIssue;
    }

    @Override
    public void doWork() {
        issueService.updateIssue(formIssue);
    }
}
