package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.entity.IssueStatus;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

import java.util.List;

import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.isNotProjectCollaborator;

public class NewIssueFormRequest extends IssueFormRequest {

    private Project project;
    private User requestUser;

    public NewIssueFormRequest(IssueService issueService, Project project, User requestUser, Model model) {
        this.issueService = issueService;
        this.project = project;
        this.requestUser = requestUser;
        this.model = model;
    }

    @Override
    public String processRequest() {

        if (project == null) return toProjects();
        if (isNotProjectCollaborator(requestUser, project)) return toAccessDenied();

        FormIssue formIssue = new FormIssue();
        formIssue.setProjectId(project.getId());

        List<IssueStatus> issueStatuses = issueService.findAllIssueStatuses();
        IssueStatus done = issueService.findIssueStatusByName("Done");

        if (done != null) {
            issueStatuses.remove(done);
        }

        model.addAttribute("allIssueTypes", issueService.findAllIssueTypes());
        model.addAttribute("allIssueStatuses", issueStatuses);
        model.addAttribute("formIssue", formIssue);

        return "dashboard/issue-form";
    }
}
