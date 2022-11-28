package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

        return "dashboard/issues";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int theId, Model model){

        Issue issue = issueService.findById(theId);

        model.addAttribute("issue", issue);

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

    @GetMapping("/deleteIssue")
    public String deleteIssue(@RequestParam("issueId") int theId, HttpServletRequest request){

        Issue issue = issueService.findById(theId);

        if(issue == null) return "redirect:/dashboard/issues";

        if(request.isUserInRole("ROLE_ADMIN") || isOwner(issue.getCreatedBy(),request.getUserPrincipal())){

            issueService.deleteById(theId);
            return "redirect:/dashboard/issues";

        }
        else return "redirect:/access-denied";
    }


    private boolean isOwner(User user, Principal principal){

        return (user != null ? (user.getUsername().equals(principal.getName())) : false);
    }
}
