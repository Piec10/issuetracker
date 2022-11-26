package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
}
