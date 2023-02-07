package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.Request;
import com.piec10.issuetracker.entity.Issue;

public abstract class IssueRequest implements Request {

    protected Issue issue;

    protected static String toAccessDenied() {
        return "redirect:/access-denied";
    }

    protected static String toProjects() {
        return "redirect:/dashboard/projects";
    }
}
