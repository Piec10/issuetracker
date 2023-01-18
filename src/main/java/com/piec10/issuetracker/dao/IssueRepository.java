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

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NULL AND project_id = :projectId ORDER BY id DESC",
            nativeQuery = true)
    List<Issue> findOpen(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NOT NULL AND project_id = :projectId ORDER BY closed_at DESC",
            nativeQuery = true)
    List<Issue> findClosed(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE project_id = :projectId ORDER BY id DESC",
            nativeQuery = true)
    List<Issue> findAll(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NULL AND project_id = :projectId ORDER BY priority ASC, id DESC",
            nativeQuery = true)
    List<Issue> findOpenSortedByPriorityAsc(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NULL AND project_id = :projectId ORDER BY priority DESC, id DESC",
            nativeQuery = true)
    List<Issue> findOpenSortedByPriorityDesc(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NOT NULL AND project_id = :projectId ORDER BY priority ASC, closed_at DESC",
            nativeQuery = true)
    List<Issue> findClosedSortedByPriorityAsc(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE closed_at IS NOT NULL AND project_id = :projectId ORDER BY priority DESC, closed_at DESC",
            nativeQuery = true)
    List<Issue> findClosedSortedByPriorityDesc(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE project_id = :projectId ORDER BY priority ASC, id DESC",
            nativeQuery = true)
    List<Issue> findAllSortedByPriorityAsc(@Param("projectId") int projectId);

    @Query(value = "SELECT * FROM issues WHERE project_id = :projectId ORDER BY priority DESC, id DESC",
            nativeQuery = true)
    List<Issue> findAllSortedByPriorityDesc(@Param("projectId") int projectId);
}
