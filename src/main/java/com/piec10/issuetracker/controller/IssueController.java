package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.issue.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.UserService;
import com.piec10.issuetracker.util.Priority;
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
    public String getIssues(@RequestParam(value = "show", required = false) String show, Model model) {

        List<Issue> issues;

        int openIssuesCount = issueService.getOpenIssuesCount();
        int closedIssuesCount = issueService.getClosedIssuesCount();

        if (show == null) {
            show = "open";
        }

        switch (show) {
            case "open":
                issues = issueService.findOpen();
                break;
            case "closed":
                issues = issueService.findClosed();
                break;
            case "all":
                issues = issueService.findAll();
                break;
            default:
                show = "open";
                issues = issueService.findOpen();
        }

        model.addAttribute("issues", issues);
        model.addAttribute("show", show);
        model.addAttribute("openIssuesCount", openIssuesCount);
        model.addAttribute("closedIssuesCount", closedIssuesCount);
        model.addAttribute("priority", new Priority());

        return "dashboard/issues";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int theId, Model model) {

        Issue issue = issueService.findById(theId);

        model.addAttribute("issue", issue);

        return "dashboard/issue-details";
    }

    @PostMapping("/processNewIssue")
    public String processNewIssue(@Valid @ModelAttribute("formIssue") FormIssue formIssue,
                                  BindingResult theBindingResult, HttpServletRequest request) {

        // form validation
        if (theBindingResult.hasErrors()) {
            return "dashboard/issue-form";
        }

        if(isNotGuest(request)){

            issueService.save(formIssue);
            return "redirect:/dashboard/issues";
        }
        else return "redirect:/access-denied";

    }


    @GetMapping("/newIssue")
    public String showNewIssueForm(Model model) {

        model.addAttribute("formIssue", new FormIssue());

        return "dashboard/issue-form";
    }

    @GetMapping("/editIssue")
    public String showEditIssueForm(@RequestParam("issueId") int theId, Model model, HttpServletRequest request) {

        Issue issue = issueService.findById(theId);
        FormIssue formIssue = new FormIssue();

        model.addAttribute("formIssue", formIssue);

        if(issue == null) return "dashboard/issue-form";

        if(isAdminOrOwner(issue.getCreatedBy(), request)){

            formIssue.setId(issue.getId());
            formIssue.setSummary(issue.getSummary());
            formIssue.setDescription(issue.getDescription());
            formIssue.setPriority(issue.getPriority());

            return "dashboard/issue-form";
        }
        else return "redirect:/access-denied";
    }

    @GetMapping("/deleteIssue")
    public String deleteIssue(@RequestParam("issueId") int theId, HttpServletRequest request) {

        Issue issue = issueService.findById(theId);

        if (issue == null) return "redirect:/dashboard/issues";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            issueService.deleteById(theId);
            return "redirect:/dashboard/issues";

        } else return "redirect:/access-denied";
    }

    @GetMapping("/closeIssue")
    public String closeIssue(@RequestParam("issueId") int theId, HttpServletRequest request) {

        Issue issue = issueService.findById(theId);

        if (issue == null) return "redirect:/dashboard/issues";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            if(issue.getClosedAt() == null){

                User closedBy = userService.findByUsername(request.getUserPrincipal().getName());
                issueService.closeIssue(theId, closedBy);
            }
            return "redirect:/dashboard/issues";

        } else return "redirect:/access-denied";
    }

    @GetMapping("/reopenIssue")
    public String reopenIssue(@RequestParam("issueId") int theId, HttpServletRequest request) {

        Issue issue = issueService.findById(theId);

        if (issue == null) return "redirect:/dashboard/issues";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            if(issue.getClosedAt() != null){

                issueService.reopenIssue(theId);
            }
            return "redirect:/dashboard/issues";

        } else return "redirect:/access-denied";
    }

    private boolean isAdminOrOwner(User user, HttpServletRequest request) {

        return request.isUserInRole("ROLE_ADMIN") || isOwner(user, request.getUserPrincipal());
    }

    private boolean isOwner(User user, Principal principal) {

        return (user != null ? (user.getUsername().equals(principal.getName())) : false);
    }

    private boolean isNotGuest(HttpServletRequest request) {
        return !request.isUserInRole("ROLE_GUEST");
    }
}
