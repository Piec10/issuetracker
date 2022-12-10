package com.piec10.issuetracker;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class IssueServiceTest {

    @MockBean
    IssueRepository issueRepository;

    @Autowired
    IssueService issueService;

    @Test
    public void findAllService() {

        Issue issue1 = new Issue();
        Issue issue2 = new Issue();
        Issue issue3 = new Issue();

        List<Issue> issues = new ArrayList<>(Arrays.asList(issue1,issue2,issue3));

        when(issueRepository.findAll()).thenReturn(issues);

        assertEquals(3,issueService.findAll().size());

    }

    @Test
    public void findByIdServiceValidId() {

        Issue issue = new Issue();
        issue.setId(1);
        when(issueRepository.findById(1)).thenReturn(Optional.of(issue));

        assertEquals(1, issueService.findById(1).getId());
    }

    @Test
    public void findByIdServiceInvalidId() {

        when(issueRepository.findById(0)).thenReturn(Optional.empty());

        assertNull(issueService.findById(0));
    }

    @Test
    public void saveNewValidIssueService() {

        FormIssue formIssue = new FormIssue();
        formIssue.setSummary("summary");

        //issueService.save(formIssue);


    }

    @Test
    public void saveUpdateIssueService() {


    }

}
