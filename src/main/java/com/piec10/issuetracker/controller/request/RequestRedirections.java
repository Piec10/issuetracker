package com.piec10.issuetracker.controller.request;

import com.piec10.issuetracker.entity.Project;

public abstract class RequestRedirections {

    public static String toAccessDenied() {
        return "redirect:/access-denied";
    }

    public static String toProjects() {
        return "redirect:/dashboard/projects";
    }

    public static String toCurrentProject(Project project) {
        return "redirect:/dashboard/issues?projectId=" + project.getId();
    }

    public static String toIssueForm() {
        return "dashboard/issue-form";
    }
}
