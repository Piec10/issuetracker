package com.piec10.issuetracker.controller;

import javax.servlet.http.HttpServletRequest;

public interface IssueRequestFactory {

    public Request createDeleteIssueRequest(int issueId, HttpServletRequest request);
}