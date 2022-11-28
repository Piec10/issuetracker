package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.issue.FormIssue;

import java.util.List;

public interface IssueService {

    List<Issue> findAll();

    Issue findById(int id);

    void save(FormIssue formIssue);

    void deleteById(int id);
}
