package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

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

        return new DeleteIssueRequest(issueService, issueId, request);
    }

    @Override
    public Request createCloseIssueRequest(int issueId, HttpServletRequest request) {

        User closedBy = userService.findByUsername(request.getUserPrincipal().getName());

        return new CloseIssueRequest(issueService, issueId, request, closedBy);
    }

    @Override
    public Request createReopenIssueRequest(int issueId, HttpServletRequest request) {

        return new ReopenIssueRequest(issueService, issueId, request);
    }

    @Override
    public Request createChangeStatusIssueRequest(int issueId, HttpServletRequest request, int statusId) {

        return new ChangeStatusIssueRequest(issueService, issueId, request, statusId);
    }

    @Override
    public Request createIssueDetailsRequest(int issueId, HttpServletRequest request, Model model) {

        Issue issue = issueService.findById(issueId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        return new IssueDetailsRequest(issue, requestUser, model);
    }

    @Override
    public Request createNewIssueRequest(int projectId, HttpServletRequest request, Model model) {

        Project project = projectService.findById(projectId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        return new NewIssueFormRequest(issueService, project, requestUser, model);
    }

    @Override
    public Request createEditIssueRequest(int issueId, HttpServletRequest request, Model model) {

        return new EditIssueFormRequest(issueService, issueId, request, model);
    }
}
