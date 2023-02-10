package com.piec10.issuetracker.controller.project;

import com.piec10.issuetracker.controller.request.Request;

import javax.servlet.http.HttpServletRequest;

public interface ProjectRequestFactory {
    Request createDeleteProjectRequest(int projectId, HttpServletRequest request);
}
