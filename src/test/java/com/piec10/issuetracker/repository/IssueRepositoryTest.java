package com.piec10.issuetracker.repository;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IssueRepositoryTest {

    @Container
    static JdbcDatabaseContainer database = new MySQLContainer("mysql:latest")
            .withPassword("springboot")
            .withUsername("springboot")
            .withInitScript("database/init.sql").withDatabaseName("springboot");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }

    @Autowired
    private IssueRepository issueRepository;

    @Test
    public void findOpenIssuesCount() {

        assertEquals(3, issueRepository.openIssuesCount(1));
    }

    @Test
    public void findClosedIssuesCount() {

        assertEquals(2,issueRepository.closedIssuesCount(1));
    }

    @Test
    public void findOpenIssues() {

        List<Issue> issues = issueRepository.findOpen(1);

        assertEquals(3, issues.size());
    }

    @Test
    public void findClosedIssues() {

        List<Issue> issues = issueRepository.findClosed(1);

        assertEquals(2, issues.size());
    }

    @Test
    public void findAllIssues() {

        List<Issue> issues = issueRepository.findAll(1);

        assertEquals(5, issues.size());
    }

    @Test
    public void findOpenIssuesSortedByPriorityAsc() {

        List<Issue> issues = issueRepository.findOpenSortedByPriorityAsc(1);

        assertEquals(0, issues.get(0).getPriority());
        assertEquals(3, issues.get(0).getId());

        assertEquals(2, issues.get(2).getPriority());
        assertEquals(1, issues.get(2).getId());
    }

    @Test
    public void findOpenIssuesSortedByPriorityDesc() {

        List<Issue> issues = issueRepository.findOpenSortedByPriorityDesc(1);

        assertEquals(2, issues.get(0).getPriority());
        assertEquals(1, issues.get(0).getId());

        assertEquals(0, issues.get(2).getPriority());
        assertEquals(3, issues.get(2).getId());
    }

    @Test
    public void findClosedIssuesSortedByPriorityAsc() {

        List<Issue> issues = issueRepository.findClosedSortedByPriorityAsc(1);

        assertEquals(0, issues.get(0).getPriority());
        assertEquals(5, issues.get(0).getId());

        assertEquals(1, issues.get(1).getPriority());
        assertEquals(4, issues.get(1).getId());
    }

    @Test
    public void findClosedIssuesSortedByPriorityDesc() {

        List<Issue> issues = issueRepository.findClosedSortedByPriorityDesc(1);

        assertEquals(1, issues.get(0).getPriority());
        assertEquals(4, issues.get(0).getId());

        assertEquals(0, issues.get(1).getPriority());
        assertEquals(5, issues.get(1).getId());
    }

    @Test
    public void findAllIssuesSortedByPriorityAsc() {

        List<Issue> issues = issueRepository.findAllSortedByPriorityAsc(1);

        assertEquals(0, issues.get(0).getPriority());
        assertEquals(5, issues.get(0).getId());

        assertEquals(2, issues.get(4).getPriority());
        assertEquals(1, issues.get(4).getId());
    }

    @Test
    public void findAllIssuesSortedByPriorityDesc() {

        List<Issue> issues = issueRepository.findAllSortedByPriorityDesc(1);

        assertEquals(2, issues.get(0).getPriority());
        assertEquals(1, issues.get(0).getId());

        assertEquals(0, issues.get(4).getPriority());
        assertEquals(3, issues.get(4).getId());
    }
}
