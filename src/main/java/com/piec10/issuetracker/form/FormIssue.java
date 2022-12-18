package com.piec10.issuetracker.form;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormIssue {

    private int id;

    @NotNull(message = "Summary is required")
    @Size(min = 1, message = "Summary is required")
    private String summary;

    private String description;

    private int priority = 1;

    private int projectId;

    public FormIssue(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
