package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.IssueStatus;
import com.piec10.issuetracker.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueStatusRepository extends JpaRepository<IssueStatus, Integer> {

    public IssueStatus findByName(String name);
}
