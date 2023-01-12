package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;

import java.util.List;

public interface IssueService {

    List<Issue> findAll(int projectId);

    Issue findById(int issueId);

    void createIssue(FormIssue formIssue, User createdBy, Project project);

    void updateIssue(FormIssue formIssue);

    void deleteById(int issueId);

    void closeIssue(int issueId, User closedBy);

    void reopenIssue(int issueId);

    int getOpenIssuesCount(int projectId);

    int getClosedIssuesCount(int projectId);

    List<Issue> findOpen(int projectId);

    List<Issue> findClosed(int projectId);

    List<Issue> findOpenPriorityAsc(int projectId);
}
