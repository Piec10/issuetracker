package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.ModificationRequest;
import com.piec10.issuetracker.controller.ModificationRequestStrategy;
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

        ModificationRequest deleteRequest = new DeleteIssueRequest(issueService, issueId, request);

        return new ModificationRequestStrategy(deleteRequest);
    }

    @Override
    public Request createCloseIssueRequest(int issueId, HttpServletRequest request) {

        User closedBy = userService.findByUsername(request.getUserPrincipal().getName());

        ModificationRequest closeIssueRequest = new CloseIssueRequest(issueService, issueId, request, closedBy);

        return new ModificationRequestStrategy(closeIssueRequest);
    }

    @Override
    public Request createReopenIssueRequest(int issueId, HttpServletRequest request) {

        ModificationRequest reopenIssueRequest = new ReopenIssueRequest(issueService, issueId, request);

        return new ModificationRequestStrategy(reopenIssueRequest);
    }

    @Override
    public Request createChangeStatusIssueRequest(int issueId, HttpServletRequest request, int statusId) {

        ModificationRequest changeStatusIssueRequest = new ChangeStatusIssueRequest(issueService, issueId, request, statusId);

        return new ModificationRequestStrategy(changeStatusIssueRequest);
    }

    @Override
    public Request createIssueDetailsRequest(int issueId, HttpServletRequest request, Model model) {

        Issue issue = issueService.findById(issueId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        ModificationRequest issueDetailsRequest = new IssueDetailsRequest(issue, requestUser, model);

        return new ModificationRequestStrategy(issueDetailsRequest);
    }

    @Override
    public Request createNewIssueRequest(int projectId, HttpServletRequest request, Model model) {

        Project project = projectService.findById(projectId);
        User requestUser = userService.findByUsername(request.getUserPrincipal().getName());

        ModificationRequest newIssueFormRequest = new NewIssueFormRequest(issueService, project, requestUser, model);

        return new ModificationRequestStrategy(newIssueFormRequest);
    }

    @Override
    public Request createEditIssueRequest(int issueId, HttpServletRequest request, Model model) {

        ModificationRequest editIssueFormRequest = new EditIssueFormRequest(issueService, issueId, request, model);

        return new ModificationRequestStrategy(editIssueFormRequest);
    }
}
