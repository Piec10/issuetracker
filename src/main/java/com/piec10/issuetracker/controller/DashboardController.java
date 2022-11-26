package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.user.FormUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IssueService issueService;

    //private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/")
    public String getDashboard(Model model){

        List<Issue> issues = issueService.findAll();

        model.addAttribute("issues",issues);

        return "dashboard/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(){
        return "dashboard/profile";
    }

    @GetMapping("/newIssue")
    public String showNewIssueForm(Model model){

        model.addAttribute("formIssue", new FormIssue());

        return "dashboard/issue-form";
    }

    @PostMapping("/processNewIssue")
    public String processNewIssue(@Valid @ModelAttribute("formIssue") FormIssue formIssue,
                                  BindingResult theBindingResult){

        // form validation
        if (theBindingResult.hasErrors()){
            return "dashboard/issue-form";
        }

        issueService.save(formIssue);

        return "redirect:/dashboard/";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int theId){

        return "dashboard/issue-details";
    }
}
