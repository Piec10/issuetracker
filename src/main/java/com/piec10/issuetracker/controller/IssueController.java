package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import com.piec10.issuetracker.util.UserProjectRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @GetMapping("/issues")
    public String getIssues(@RequestParam(value = "projectId") int projectId,
                            @RequestParam(value = "show", required = false) String show,
                            @RequestParam(value = "sort", required = false) String sort,
                            Model model,
                            HttpServletRequest request) {

        Project project = projectService.findById(projectId);

        if(project == null) return "redirect:/dashboard/projects";

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        UserProjectRoles userProjectRoles = getUserProjectRoles(project, currentUser);

        if(isAdmin(request) || userProjectRoles.isFollower()){

            List<Issue> issues;

            int openIssuesCount = issueService.getOpenIssuesCount(projectId);
            int closedIssuesCount = issueService.getClosedIssuesCount(projectId);

            if (show == null) {
                show = "open";
            }
            if (sort == null) {
                sort = "noop";
            }

            issues = getIssues(projectId, show, sort);

            model.addAttribute("issues", issues);
            model.addAttribute("show", show);
            model.addAttribute("sort", sort);
            model.addAttribute("openIssuesCount", openIssuesCount);
            model.addAttribute("closedIssuesCount", closedIssuesCount);
            model.addAttribute("project", project);
            model.addAttribute("projectRoles", userProjectRoles);

            return "dashboard/issues";
        }
        else return "redirect:/access-denied";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int issueId, Model model, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);

        if(issue == null) return "redirect:/dashboard/projects";

        Project project = issue.getProject();

        if(project == null) return "redirect:/dashboard/projects";

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        UserProjectRoles userProjectRoles = getUserProjectRoles(project, currentUser);

        if(isAdmin(request) || userProjectRoles.isFollower()){

            model.addAttribute("issue", issue);

            return "dashboard/issue-details";
        }
        else return "redirect:/access-denied";

    }

    @GetMapping("/newIssue")
    public String showNewIssueForm(@RequestParam(value = "projectId") int projectId,
                                   Model model, Principal principal) {

        Project project = projectService.findById(projectId);

        if(project == null) return "redirect:/dashboard/projects";

        User currentUser = userService.findByUsername(principal.getName());
        UserProjectRoles userProjectRoles = getUserProjectRoles(project, currentUser);

        if(userProjectRoles.isCollaborator()){

            FormIssue formIssue = new FormIssue();
            formIssue.setProjectId(projectId);

            model.addAttribute("formIssue", formIssue);

            return "dashboard/issue-form";
        }
        else return "redirect:/access-denied";

    }

    @GetMapping("/editIssue")
    public String showEditIssueForm(@RequestParam("issueId") int issueId, Model model, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);

        if(issue == null) return "redirect:/dashboard/projects";

        if(isAdminOrOwner(issue.getCreatedBy(), request)){

            FormIssue formIssue = new FormIssue();

            formIssue.setId(issue.getId());
            formIssue.setSummary(issue.getSummary());
            formIssue.setDescription(issue.getDescription());
            formIssue.setPriority(issue.getPriority());
            formIssue.setProjectId(issue.getProject().getId());

            model.addAttribute("formIssue", formIssue);

            return "dashboard/issue-form";
        }
        else return "redirect:/access-denied";
    }

    @PostMapping("/processIssue")
    public String processIssue(@Valid @ModelAttribute("formIssue") FormIssue formIssue,
                                  BindingResult theBindingResult, HttpServletRequest request, Model model) {

        // form validation
        if (theBindingResult.hasErrors()) return "dashboard/issue-form";

        if (isGuest(request)) {

            model.addAttribute("guestUserError", "Sorry, Guest user cannot create new issues.");
            return "dashboard/issue-form";
        }


        Project project = projectService.findById(formIssue.getProjectId());

        if(project == null) return "redirect:/dashboard/projects";

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        UserProjectRoles userProjectRoles = getUserProjectRoles(project, currentUser);

        if(formIssue.getId() == 0){

            if(userProjectRoles.isCollaborator()) {

                User createdBy = userService.findByUsername(request.getUserPrincipal().getName());

                issueService.createIssue(formIssue, createdBy, project);
                return "redirect:/dashboard/issues?projectId=" + project.getId();
            }
            else return "redirect:/access-denied";
        }

        Issue issue = issueService.findById(formIssue.getId());

        if(issue == null) return "redirect:/dashboard/issues?projectId=" + project.getId();

        if(isAdminOrOwner(issue.getCreatedBy(),request)){

            issueService.updateIssue(formIssue);
            return "redirect:/dashboard/issues?projectId=" + project.getId();
        }
        else return "redirect:/access-denied";
    }

    @DeleteMapping("/deleteIssue/{issueId}")
    public String deleteIssue(@PathVariable int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);

        if (issue == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            issueService.deleteById(issueId);
            return "redirect:/dashboard/issues?projectId=" + issue.getProject().getId();

        } else return "redirect:/access-denied";
    }

    @PatchMapping("/closeIssue/{issueId}")
    public String closeIssue(@PathVariable int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);

        if (issue == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            if(issue.getClosedAt() == null){

                User closedBy = userService.findByUsername(request.getUserPrincipal().getName());
                issueService.closeIssue(issueId, closedBy);
            }
            return "redirect:/dashboard/issues?projectId=" + issue.getProject().getId();

        } else return "redirect:/access-denied";
    }

    @PatchMapping("/reopenIssue/{issueId}")
    public String reopenIssue(@PathVariable int issueId, HttpServletRequest request) {

        Issue issue = issueService.findById(issueId);

        if (issue == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(issue.getCreatedBy(), request)) {

            if(issue.getClosedAt() != null){

                issueService.reopenIssue(issueId);
            }
            return "redirect:/dashboard/issues?projectId=" + issue.getProject().getId();

        } else return "redirect:/access-denied";
    }

    private List<Issue> getIssues(int projectId, String show, String sort) {

        if(show.equals("open")) {

            if(sort.equals("noop")) return issueService.findOpen(projectId);

            if(sort.equals("priorityAsc")) return issueService.findOpenPriorityAsc(projectId);

            if(sort.equals("priorityDesc")) return issueService.findOpenPriorityDesc(projectId);
        }

        if(show.equals("closed")) {

            if(sort.equals("noop")) return issueService.findClosed(projectId);

            if(sort.equals("priorityAsc")) return issueService.findClosedPriorityAsc(projectId);

            if(sort.equals("priorityDesc")) return issueService.findClosedPriorityDesc(projectId);
        }

        if(show.equals("all")) {

            if(sort.equals("noop")) return issueService.findAll(projectId);

            if(sort.equals("priorityAsc")) return issueService.findAllPriorityAsc(projectId);

            if(sort.equals("priorityDesc")) return issueService.findAllPriorityDesc(projectId);
        }

        return null;
    }
}
