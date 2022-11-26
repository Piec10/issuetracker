package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }
}
