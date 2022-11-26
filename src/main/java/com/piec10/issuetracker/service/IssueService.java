package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Issue;

import java.util.List;

public interface IssueService {

    public List<Issue> findAll();
}
