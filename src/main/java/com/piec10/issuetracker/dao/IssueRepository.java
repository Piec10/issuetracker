package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "SELECT COUNT(*) FROM issues  WHERE closed_at IS NULL",
            nativeQuery = true)
    int openIssuesCount();

    @Query(value = "SELECT COUNT(*) FROM issues  WHERE closed_at IS NOT NULL",
            nativeQuery = true)
    int closedIssuesCount();
}
