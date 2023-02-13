package com.piec10.issuetracker.controller.request.issue;

import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.form.FormIssue;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface IssueRequestFactory {

    Request createDeleteIssueRequest(int issueId, HttpServletRequest request);

    Request createCloseIssueRequest(int issueId, HttpServletRequest request);

    Request createReopenIssueRequest(int issueId, HttpServletRequest request);

    Request createChangeStatusIssueRequest(int issueId, HttpServletRequest request, int statusId);

    Request createIssueDetailsRequest(int issueId, HttpServletRequest request, Model model);

    Request createNewIssueRequest(int projectId, HttpServletRequest request, Model model);

    Request createEditIssueRequest(int issueId, HttpServletRequest request, Model model);

    Request createProcessIssueRequest(FormIssue formIssue, BindingResult bindingResult, HttpServletRequest request, Model model);

    Request createUpdateIssueRequest(HttpServletRequest request, FormIssue formIssue);

    Request getCreateIssueRequest(HttpServletRequest request, FormIssue formIssue);
}