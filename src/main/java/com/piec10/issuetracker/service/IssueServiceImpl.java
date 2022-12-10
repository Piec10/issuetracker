package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue findById(int id) {
        Optional<Issue> issue =  issueRepository.findById(id);

        if(issue.isPresent()){
            return issue.get();
        }

        else return null;
    }

    @Override
    public void createIssue(FormIssue formIssue, User createdBy) {

        Issue newIssue = new Issue();
        newIssue.setSummary(formIssue.getSummary());
        newIssue.setDescription(formIssue.getDescription());
        newIssue.setPriority(formIssue.getPriority());
        newIssue.setCreatedBy(createdBy);
        newIssue.setCreatedAt(new Date());

        issueRepository.save(newIssue);
    }

    @Override
    public void updateIssue(FormIssue formIssue) {

        Issue issue = findById(formIssue.getId());
        issue.setSummary(formIssue.getSummary());
        issue.setDescription(formIssue.getDescription());
        issue.setPriority(formIssue.getPriority());

        issueRepository.save(issue);
    }

    @Override
    public void deleteById(int id) {
        issueRepository.deleteById(id);
    }

    @Override
    public void closeIssue(int theId, User closedBy) {

        Issue issue = findById(theId);
        issue.setClosedBy(closedBy);
        issue.setClosedAt(new Date());

        issueRepository.save(issue);
    }

    @Override
    public void reopenIssue(int theId) {

        Issue issue = findById(theId);
        issue.setClosedBy(null);
        issue.setClosedAt(null);

        issueRepository.save(issue);
    }

    @Override
    public int getOpenIssuesCount() {

        return issueRepository.openIssuesCount();
    }

    @Override
    public int getClosedIssuesCount() {
        return issueRepository.closedIssuesCount();
    }

    @Override
    public List<Issue> findOpen() {
        return issueRepository.findOpen();
    }

    @Override
    public List<Issue> findClosed() {
        return issueRepository.findClosed();
    }
}
