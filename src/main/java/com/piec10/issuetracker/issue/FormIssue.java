package com.piec10.issuetracker.issue;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormIssue {

    @NotNull(message = "Summary is required")
    @Size(min = 1, message = "Summary is required")
    private String summary;


    private String description;

    private int priority = 1;

    public FormIssue(){}

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
}