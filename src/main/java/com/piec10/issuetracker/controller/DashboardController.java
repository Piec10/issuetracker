package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IssueService issueService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/")
    public String getDashboard(Model model){

        List<Issue> issues = issueService.findAll();

        //logger.info(issues.get(0).getCreatedBy().toString());

        model.addAttribute("issues",issues);

        return "dashboard/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(){
        return "dashboard/profile";
    }

    @GetMapping("/newIssue")
    public String showNewIssueForm(){
        return "dashboard/issue-form";
    }

}
