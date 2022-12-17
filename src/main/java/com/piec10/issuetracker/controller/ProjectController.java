package com.piec10.issuetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class ProjectController {

    @GetMapping("/projects")
    public String getProjects() {

        return "dashboard/projects";
    }
}
