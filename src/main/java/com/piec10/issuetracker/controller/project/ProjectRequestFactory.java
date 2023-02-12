package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.controller.request.Request;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public interface ProjectRequestFactory {
    Request createDeleteProjectRequest(int projectId, HttpServletRequest request);

    Request createEditProjectRequest(int projectId, HttpServletRequest request, Model model);
}
