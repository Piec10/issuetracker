package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
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
    public List<Issue> findAll(int projectId) {
        return issueRepository.findAll(projectId);
    }

    @Override
    public Issue findById(int issueId) {
        Optional<Issue> issue =  issueRepository.findById(issueId);

        if(issue.isPresent()){
            return issue.get();
        }

        else return null;
    }

    @Override
    public void createIssue(FormIssue formIssue, User createdBy, Project project) {

        Issue newIssue = new Issue();
        newIssue.setSummary(formIssue.getSummary());
        newIssue.setDescription(formIssue.getDescription());
        newIssue.setPriority(formIssue.getPriority());
        newIssue.setCreatedBy(createdBy);
        newIssue.setCreatedAt(new Date());
        newIssue.setProject(project);

        issueRepository.save(newIssue);
    }

    @Override
    public void updateIssue(FormIssue formIssue) {

        Issue issue = findById(formIssue.getId());

        if(issue != null) {
            issue.setSummary(formIssue.getSummary());
            issue.setDescription(formIssue.getDescription());
            issue.setPriority(formIssue.getPriority());

            issueRepository.save(issue);
        }
    }

    @Override
    public void deleteById(int issueId) {
        issueRepository.deleteById(issueId);
    }

    @Override
    public void closeIssue(int issueId, User closedBy) {

        Issue issue = findById(issueId);

        if(issue != null) {
            issue.setClosedBy(closedBy);
            issue.setClosedAt(new Date());

            issueRepository.save(issue);
        }
    }

    @Override
    public void reopenIssue(int issueId) {

        Issue issue = findById(issueId);

        if(issue != null) {
            issue.setClosedBy(null);
            issue.setClosedAt(null);

            issueRepository.save(issue);
        }
    }

    @Override
    public int getOpenIssuesCount(int projectId) {

        return issueRepository.openIssuesCount(projectId);
    }

    @Override
    public int getClosedIssuesCount(int projectId) {
        return issueRepository.closedIssuesCount(projectId);
    }

    @Override
    public List<Issue> findOpen(int projectId) {
        return issueRepository.findOpen(projectId);
    }

    @Override
    public List<Issue> findClosed(int projectId) {
        return issueRepository.findClosed(projectId);
    }

    @Override
    public List<Issue> findOpenPriorityAsc(int projectId) {
        return issueRepository.findOpenSortedByPriorityAsc(projectId);
    }
}
