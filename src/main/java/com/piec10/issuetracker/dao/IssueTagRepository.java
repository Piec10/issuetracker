package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.IssueTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueTagRepository extends JpaRepository<IssueTag, Integer> {

}
