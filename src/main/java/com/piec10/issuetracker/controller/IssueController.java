package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/dashboard")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    //private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/issues")
    public String getIssues(Model model){

        List<Issue> issues = issueService.findAll();

        int openIssuesCount = issueService.getOpenIssuesCount();
        int closedIssuesCount = issueService.getClosedIssuesCount();

        model.addAttribute("issues",issues);
        model.addAttribute("openIssuesCount", openIssuesCount);
        model.addAttribute("closedIssuesCount", closedIssuesCount);

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

        if(isAdminOrOwner(issue.getCreatedBy(), request)){

            issueService.deleteById(theId);
            return "redirect:/dashboard/issues";

        }
        else return "redirect:/access-denied";
    }

    @GetMapping("/closeIssue")
    public String closeIssue(@RequestParam("issueId") int theId, HttpServletRequest request) {

        Issue issue = issueService.findById(theId);

        if(issue == null) return "redirect:/dashboard/issues";

        if(isAdminOrOwner(issue.getCreatedBy(), request)){

            User closedBy = userService.findByUsername(request.getUserPrincipal().getName());

            issueService.closeIssue(theId,closedBy);
            return "redirect:/dashboard/issues";

        }
        else return "redirect:/access-denied";

    }

    private boolean isAdminOrOwner(User user, HttpServletRequest request){

        return request.isUserInRole("ROLE_ADMIN") || isOwner(user,request.getUserPrincipal());
    }

    private boolean isOwner(User user, Principal principal){

        return (user != null ? (user.getUsername().equals(principal.getName())) : false);
    }
}
