package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.dao.IssueStatusRepository;
import com.piec10.issuetracker.dao.IssueTagRepository;
import com.piec10.issuetracker.dao.IssueTypeRepository;
import com.piec10.issuetracker.entity.*;
import com.piec10.issuetracker.form.FormIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueTypeRepository issueTypeRepository;

    @Autowired
    private IssueStatusRepository issueStatusRepository;

    @Autowired
    private IssueTagRepository issueTagRepository;

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

        Collection<IssueTag> issueTags = formIssue.getIssueTags().stream()
                .map(issueTagId -> findIssueTagById(issueTagId)).collect(Collectors.toList());

        newIssue.setIssueTags(issueTags);

        newIssue.setIssueType(findIssueTypeById(formIssue.getIssueTypeId()));
        newIssue.setIssueStatus(findIssueStatusById(formIssue.getIssueStatusId()));

        issueRepository.save(newIssue);
    }

    @Override
    public void updateIssue(FormIssue formIssue) {

        Issue issue = findById(formIssue.getId());

        if(issue != null) {
            issue.setSummary(formIssue.getSummary());
            issue.setDescription(formIssue.getDescription());
            issue.setPriority(formIssue.getPriority());

            Collection<IssueTag> issueTags = formIssue.getIssueTags().stream()
                    .map(issueTagId -> findIssueTagById(issueTagId)).collect(Collectors.toList());

            issue.setIssueTags(issueTags);

            issue.setIssueType(findIssueTypeById(formIssue.getIssueTypeId()));
            issue.setIssueStatus(findIssueStatusById(formIssue.getIssueStatusId()));

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

    @Override
    public List<Issue> findOpenPriorityDesc(int projectId) {
        return issueRepository.findOpenSortedByPriorityDesc(projectId);
    }

    @Override
    public List<Issue> findClosedPriorityAsc(int projectId) {
        return issueRepository.findClosedSortedByPriorityAsc(projectId);
    }

    @Override
    public List<Issue> findClosedPriorityDesc(int projectId) {
        return issueRepository.findClosedSortedByPriorityDesc(projectId);
    }

    @Override
    public List<Issue> findAllPriorityAsc(int projectId) {
        return issueRepository.findAllSortedByPriorityAsc(projectId);
    }

    @Override
    public List<Issue> findAllPriorityDesc(int projectId) {
        return issueRepository.findAllSortedByPriorityDesc(projectId);
    }

    @Override
    public IssueType findIssueTypeById(int issueTypeId) {

        Optional<IssueType> issueType = issueTypeRepository.findById(issueTypeId);

        if(issueType.isPresent()) return issueType.get();
        else return null;
    }

    @Override
    public IssueType findIssueTypeByName(String name) {

        return issueTypeRepository.findByName(name);
    }

    @Override
    public IssueStatus findIssueStatusById(int issueStatusId) {

        Optional<IssueStatus> issueStatus = issueStatusRepository.findById(issueStatusId);

        if(issueStatus.isPresent()) return issueStatus.get();
        else return null;
    }

    @Override
    public IssueStatus findIssueStatusByName(String name) {

        return issueStatusRepository.findByName(name);
    }

    @Override
    public IssueTag findIssueTagById(int issueTagId) {

        Optional<IssueTag> issueTag = issueTagRepository.findById(issueTagId);

        if(issueTag.isPresent()) return issueTag.get();
        else return null;
    }

    @Override
    public List<IssueType> findAllIssueTypes() {
        return issueTypeRepository.findAll();
    }

    @Override
    public List<IssueStatus> findAllIssueStatuses() {
        return issueStatusRepository.findAll();
    }

    @Override
    public void changeIssueStatus(Issue issue, int statusId) {
        IssueStatus issueStatus = findIssueStatusById(statusId);
        if(issueStatus != null) {
            issue.setIssueStatus(issueStatus);
            issueRepository.save(issue);
        }
    }
}
