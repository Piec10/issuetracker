package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "SELECT COUNT(*) FROM issues WHERE closed_at IS NULL AND project_id = :projectId",
            nativeQuery = true)
    int openIssuesCount(@Param("projectId") int projectId);

    @Query(value = "SELECT COUNT(*) FROM issues WHERE closed_at IS NOT NULL AND project_id = :projectId",
            nativeQuery = true)
    int closedIssuesCount(@Param("projectId")int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NULL ORDER BY id DESC",
            nativeQuery = true)
    List<Issue> findOpen();

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NOT NULL ORDER BY closed_at DESC",
            nativeQuery = true)
    List<Issue> findClosed();

    @Override
    @Query(value = "SELECT * FROM issues ORDER BY id DESC",
            nativeQuery = true)
    List<Issue> findAll();
}
