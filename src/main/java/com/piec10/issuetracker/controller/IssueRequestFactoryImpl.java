package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IssueRequestFactoryImpl implements IssueRequestFactory {

    @Autowired
    private IssueService issueService;

    @Override
    public Request createDeleteIssueRequest(int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);
        return new IssueDeleteRequest(issueService, issue, request);
    }
}
