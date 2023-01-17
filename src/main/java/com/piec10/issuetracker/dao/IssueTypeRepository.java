package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueTypeRepository extends JpaRepository<IssueType, Integer> {

}
