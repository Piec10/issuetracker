package com.piec10.issuetracker.dto;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;


public class ProjectDto extends Project {

    private int openIssuesCount;
    private int closedIssuesCount;

    public ProjectDto(Project p, int openIssuesCount, int closedIssuesCount) {
        this.setId(p.getId());
        this.setTitle(p.getTitle());
        this.setDescription(p.getDescription());
        this.setCreatedBy(p.getCreatedBy());
        this.setCreatedAt(p.getCreatedAt());
        this.setIssues(p.getIssues());
        this.setFollowers(p.getFollowers());
        this.setCollaborators(p.getCollaborators());
        this.openIssuesCount = openIssuesCount;
        this.closedIssuesCount = closedIssuesCount;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public int getClosedIssuesCount() {
        return closedIssuesCount;
    }

    public void setClosedIssuesCount(int closedIssuesCount) {
        this.closedIssuesCount = closedIssuesCount;
    }
}
