package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.*;
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

    List<Issue> findOpenPriorityDesc(int projectId);

    List<Issue> findClosedPriorityAsc(int projectId);

    List<Issue> findClosedPriorityDesc(int projectId);

    List<Issue> findAllPriorityAsc(int projectId);

    List<Issue> findAllPriorityDesc(int projectId);

    IssueType findIssueTypeById(int issueTypeId);

    IssueTag findIssueTagById(int issueTagId);

    List<IssueType> findAllIssueTypes();
}
