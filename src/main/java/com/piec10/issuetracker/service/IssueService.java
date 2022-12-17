package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;

import java.util.List;

public interface IssueService {

    List<Issue> findAll();

    Issue findById(int id);

    void createIssue(FormIssue formIssue, User createdBy);

    void updateIssue(FormIssue formIssue);

    void deleteById(int id);

    void closeIssue(int theId, User closedBy);

    void reopenIssue(int theId);

    int getOpenIssuesCount(int projectId);

    int getClosedIssuesCount(int projectId);

    List<Issue> findOpen();

    List<Issue> findClosed();

}
