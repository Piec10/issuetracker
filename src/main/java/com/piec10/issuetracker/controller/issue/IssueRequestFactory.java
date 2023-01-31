package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;

import javax.servlet.http.HttpServletRequest;

public interface IssueRequestFactory {

    Request createDeleteIssueRequest(int issueId, HttpServletRequest request);

    Request createCloseIssueRequest(int issueId, HttpServletRequest request);
}