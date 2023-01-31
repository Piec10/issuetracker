package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;
import com.piec10.issuetracker.controller.issue.CloseIssueRequest;
import com.piec10.issuetracker.controller.issue.DeleteIssueRequest;
import com.piec10.issuetracker.controller.issue.IssueRequestFactory;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IssueRequestFactoryImpl implements IssueRequestFactory {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Override
    public Request createDeleteIssueRequest(int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);
        return new DeleteIssueRequest(issueService, issue, request);
    }

    @Override
    public Request createCloseIssueRequest(int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);
        User closedBy = userService.findByUsername(request.getUserPrincipal().getName());

        return new CloseIssueRequest(issueService, issue, request, closedBy);
    }
}
