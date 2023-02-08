package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.doesNotHavePermissionToModify;

public class EditIssueFormRequest extends IssueFormRequest{

    HttpServletRequest request;

    public EditIssueFormRequest(IssueService issueService, int issueId, HttpServletRequest request, Model model) {
        this.issueService = issueService;
        this.issue = issueService.findById(issueId);
        this.request = request;
        this.model = model;
    }

    @Override
    public String processRequest() {

        if (issue == null) return toProjects();
        if (doesNotHavePermissionToModify(issue, request)) return toAccessDenied();

        FormIssue formIssue = new FormIssue();

        formIssue.setId(issue.getId());
        formIssue.setSummary(issue.getSummary());
        formIssue.setDescription(issue.getDescription());
        formIssue.setPriority(issue.getPriority());
        formIssue.setProjectId(issue.getProject().getId());

        List<Integer> issueTags = issue.getIssueTags().stream()
                .map(issueTag -> issueTag.getId()).collect(Collectors.toList());

        formIssue.setIssueTags(issueTags);

        if (issue.getIssueType() != null) {
            formIssue.setIssueTypeId(issue.getIssueType().getId());
        }

        if (issue.getIssueStatus() != null) {
            formIssue.setIssueStatusId(issue.getIssueStatus().getId());
        }

        model.addAttribute("allIssueTypes", issueService.findAllIssueTypes());
        model.addAttribute("allIssueStatuses", issueService.findAllIssueStatuses());
        model.addAttribute("formIssue", formIssue);

        return "dashboard/issue-form";
    }
}
