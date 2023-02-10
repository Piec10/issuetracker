package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.ModificationRequest;
import com.piec10.issuetracker.entity.IssueStatus;
import com.piec10.issuetracker.entity.IssueType;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;
import java.util.List;

public abstract class IssueFormRequest extends IssueRequest implements ModificationRequest {

    protected IssueService issueService;

    protected Model model;

    protected FormIssue formIssue = new FormIssue();
    protected List<IssueStatus> allIssueStatuses;
    protected List<IssueType> allIssueTypes;

    public IssueFormRequest(IssueService issueService, Model model) {
        this.issueService = issueService;
        this.model= model;
        allIssueStatuses = issueService.findAllIssueStatuses();
        allIssueTypes = issueService.findAllIssueTypes();
    }

    @Override
    public void modify() {
        prepareModelAttributes();
        addModelAttributes();
    }

    protected abstract void prepareModelAttributes();
    private void addModelAttributes() {
        model.addAttribute("allIssueTypes", allIssueTypes);
        model.addAttribute("allIssueStatuses", allIssueStatuses);
        model.addAttribute("formIssue", formIssue);
    }

    @Override
    public String redirectWhenSuccess() {
        return toIssueForm();
    }

    protected String toIssueForm() {
        return "dashboard/issue-form";
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
