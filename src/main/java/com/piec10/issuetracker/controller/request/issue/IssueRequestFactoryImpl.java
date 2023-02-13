package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.controller.request.*;
import com.piec10.issuetracker.controller.request.issue.form.EditIssueFormRequest;
import com.piec10.issuetracker.controller.request.issue.form.NewIssueFormRequest;
import com.piec10.issuetracker.controller.request.issue.form.ProcessIssueFormRequest;
import com.piec10.issuetracker.controller.request.issue.modification.*;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

@Component
public class IssueRequestFactoryImpl implements IssueRequestFactory {

    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @Override
    public Request createDeleteIssueRequest(int issueId, HttpServletRequest request) {

        RestrictedAccessRequest deleteRequest = new DeleteIssueRequest(issueService, issueId, request);

        return new RestrictedAccessRequestStrategy(deleteRequest);
    }

    @Override
    public Request createCloseIssueRequest(int issueId, HttpServletRequest request) {

        User closedBy = userService.findByUsername(request.getUserPrincipal().getName());

        RestrictedAccessRequest closeIssueRequest = new CloseIssueRequest(issueService, issueId, request, closedBy);

        return new RestrictedAccessRequestStrategy(closeIssueRequest);
    }

    @Override
    public Request createReopenIssueRequest(int issueId, HttpServletRequest request) {

        RestrictedAccessRequest reopenIssueRequest = new ReopenIssueRequest(issueService, issueId, request);

        return new RestrictedAccessRequestStrategy(reopenIssueRequest);
    }

    @Override
    public Request createChangeStatusIssueRequest(int issueId, HttpServletRequest request, int statusId) {

        RestrictedAccessRequest changeStatusIssueRequest = new ChangeStatusIssueRequest(issueService, issueId, request, statusId);

        return new RestrictedAccessRequestStrategy(changeStatusIssueRequest);
    }

    @Override
    public Request createIssueDetailsRequest(int issueId, HttpServletRequest request, Model model) {

        Issue issue = issueService.findById(issueId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        RestrictedAccessRequest issueDetailsRequest = new IssueDetailsRequest(issueService, issue, requestUser, model);

        return new RestrictedAccessRequestStrategy(issueDetailsRequest);
    }

    @Override
    public Request createNewIssueRequest(int projectId, HttpServletRequest request, Model model) {

        Project project = projectService.findById(projectId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        RestrictedAccessRequest newIssueFormRequest = new NewIssueFormRequest(issueService, project, requestUser, model);

        return new RestrictedAccessRequestStrategy(newIssueFormRequest);
    }

    @Override
    public Request createEditIssueRequest(int issueId, HttpServletRequest request, Model model) {

        RestrictedAccessRequest editIssueFormRequest = new EditIssueFormRequest(issueService, issueId, request, model);

        return new RestrictedAccessRequestStrategy(editIssueFormRequest);
    }

    @Override
    public Request createProcessIssueRequest(FormIssue formIssue, BindingResult bindingResult, HttpServletRequest request, Model model) {

        ProcessFormRequest processFormRequest = new ProcessIssueFormRequest(this, issueService, formIssue, bindingResult, request, model);

        return new ProcessFormRequestStrategy(processFormRequest);
    }

    @Override
    public Request createUpdateIssueRequest(HttpServletRequest request, FormIssue formIssue) {

        RestrictedAccessRequest updateIssueRequest = new UpdateIssueRequest(issueService, formIssue.getId(), request, formIssue);

        return new RestrictedAccessRequestStrategy(updateIssueRequest);
    }

    @Override
    public Request getCreateIssueRequest(HttpServletRequest request, FormIssue formIssue) {

        Project project = projectService.findById(formIssue.getProjectId());
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        RestrictedAccessRequest createIssueRequest = new CreateIssueRequest(issueService, project, request, requestUser, formIssue);

        return new RestrictedAccessRequestStrategy(createIssueRequest);
    }

    @Override
    public Request createChangeTypeIssueRequest(int issueId, HttpServletRequest request, int typeId) {
        RestrictedAccessRequest changeTypeIssueRequest = new ChangeTypeIssueRequest(issueService, issueId, request, typeId);

        return new RestrictedAccessRequestStrategy(changeTypeIssueRequest);
    }
}
