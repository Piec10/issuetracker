package com.piec10.issuetracker.controller.request.issue.modification;

import com.piec10.issuetracker.controller.request.issue.IssueRestrictedAccessRequest;
import com.piec10.issuetracker.service.IssueService;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.controller.request.RequestRedirections.*;

public abstract class IssueModificationRequest extends IssueRestrictedAccessRequest {

    public IssueModificationRequest(IssueService issueService, int issueId, HttpServletRequest request) {
        super(issueService, issueId, request);
    }

    @Override
    public String redirectWhenSuccess() {
        return toCurrentProject(issue.getProject());
    }
}
