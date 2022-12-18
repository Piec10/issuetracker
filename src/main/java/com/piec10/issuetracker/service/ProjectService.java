package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.Project;

public interface ProjectService {
    Project findById(int projectId);
}
