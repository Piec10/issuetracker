package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.controller.request.RestrictedAccessRequest;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

import static com.piec10.issuetracker.controller.request.RequestRedirections.*;
import static com.piec10.issuetracker.controller.request.RequestRedirections.toAccessDenied;
import static com.piec10.issuetracker.controller.request.RequestRedirections.toProjects;

public abstract class RestrictedAccessIssueFormRequest extends IssueFormRequest implements RestrictedAccessRequest {

    public RestrictedAccessIssueFormRequest(IssueService issueService, Model model) {
        super(issueService, model);
    }

    @Override
    public void doWork() {
        prepareModelAttributes();
        addModelAttributes();
    }

    protected abstract void prepareModelAttributes();

    @Override
    public String redirectWhenSuccess() {
        return toIssueForm();
    }

    @Override
    public String redirectWhenNull() {
        return toProjects();
    }

    @Override
    public String redirectWhenNoPermission() {
        return toAccessDenied();
    }
}
