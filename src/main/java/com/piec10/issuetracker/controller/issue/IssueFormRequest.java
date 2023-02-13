package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.IssueStatus;
import com.piec10.issuetracker.entity.IssueType;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

import java.util.List;

public abstract class IssueFormRequest {

    protected Model model;
    protected FormIssue formIssue = new FormIssue();
    protected List<IssueStatus> allIssueStatuses;
    protected List<IssueType> allIssueTypes;
    protected IssueService issueService;
    protected Issue issue;

    public IssueFormRequest(IssueService issueService, Model model) {
        this.issueService = issueService;
        this.model= model;
        allIssueStatuses = issueService.findAllIssueStatuses();
        allIssueTypes = issueService.findAllIssueTypes();
    }

    protected void addModelAttributes() {
        model.addAttribute("allIssueTypes", allIssueTypes);
        model.addAttribute("allIssueStatuses", allIssueStatuses);
        model.addAttribute("formIssue", formIssue);
    }

    protected String toIssueForm() {
        return "dashboard/issue-form";
    }
}
