package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping("/issues")
    public String getIssues(Model model){

        List<Issue> issues = issueService.findAll();

        model.addAttribute("issues",issues);

        return "dashboard/dashboard";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int theId, Principal principal, Model model){

        Issue issue = issueService.findById(theId);
        boolean isOwner = false;

        if(issue.getCreatedBy() != null){
            if(issue.getCreatedBy().getUsername().equals(principal.getName())) {
                isOwner = true;
            }
        }

        model.addAttribute("issue", issue);
        model.addAttribute("isOwner", isOwner);

        return "dashboard/issue-details";
    }

    @PostMapping("/processNewIssue")
    public String processNewIssue(@Valid @ModelAttribute("formIssue") FormIssue formIssue,
                                  BindingResult theBindingResult){

        // form validation
        if (theBindingResult.hasErrors()){
            return "dashboard/issue-form";
        }

        issueService.save(formIssue);

        return "redirect:/dashboard/issues";
    }

    @GetMapping("/newIssue")
    public String showNewIssueForm(Model model){

        model.addAttribute("formIssue", new FormIssue());

        return "dashboard/issue-form";
    }
}
