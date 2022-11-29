package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.dao.UserRepository;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

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
    public void save(FormIssue formIssue) {

        Issue newIssue = new Issue();
        newIssue.setSummary(formIssue.getSummary());
        newIssue.setDescription(formIssue.getDescription());
        newIssue.setPriority(formIssue.getPriority());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUserName);

        newIssue.setCreatedBy(currentUser);
        newIssue.setCreatedAt(new Date());

        issueRepository.save(newIssue);
    }

    @Override
    public void deleteById(int id) {
        issueRepository.deleteById(id);
    }

    @Override
    public void closeIssue(int theId, User closedBy) {

        Issue issue = findById(theId);

    }
}
