package com.piec10.issuetracker.dto;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoWrapper {

    @Autowired
    private IssueService issueService;

    public ProjectDto wrap(Project project) {
        int openIssuesCount = issueService.getOpenIssuesCount(project.getId());
        int closedIssuesCount = issueService.getClosedIssuesCount(project.getId());

        return new ProjectDto(project, openIssuesCount, closedIssuesCount);
    }
}
