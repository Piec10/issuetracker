package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueStatusRepository extends JpaRepository<IssueStatus, Integer> {
}
