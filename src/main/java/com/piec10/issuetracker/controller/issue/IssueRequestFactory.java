package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public interface IssueRequestFactory {

    Request createDeleteIssueRequest(int issueId, HttpServletRequest request);

    Request createCloseIssueRequest(int issueId, HttpServletRequest request);

    Request createReopenIssueRequest(int issueId, HttpServletRequest request);

    Request createChangeStatusIssueRequest(int issueId, HttpServletRequest request, int statusId);

    Request createIssueDetailsRequest(int issueId, HttpServletRequest request, Model model);
}