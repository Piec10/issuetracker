package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/")
    public String getDashboard(){
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
