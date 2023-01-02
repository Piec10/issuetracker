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

import static com.piec10.issuetracker.config.GlobalRolesAndOwnerCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    //private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/issues")
    public String getIssues(@RequestParam(value = "projectId") int projectId,
                            @RequestParam(value = "show", required = false) String show,
                            Model model,
                            HttpServletRequest request) {

        Project project = projectService.findById(projectId);

        if(project == null) return "redirect:/dashboard/projects";

        UserProjectRoles userProjectRoles = getUserProjectRoles(project, request.getUserPrincipal());

        if(isAdmin(request) || userProjectRoles.isGuest()){

            List<Issue> issues;

            int openIssuesCount = issueService.getOpenIssuesCount(projectId);
            int closedIssuesCount = issueService.getClosedIssuesCount(projectId);

            if (show == null) {
                show = "open";
            }

            switch (show) {
                case "open":
                    issues = issueService.findOpen(projectId);
                    break;
                case "closed":
                    issues = issueService.findClosed(projectId);
                    break;
                case "all":
                    issues = issueService.findAll(projectId);
                    break;
                default:
                    show = "open";
                    issues = issueService.findOpen(projectId);
            }

            model.addAttribute("issues", issues);
            model.addAttribute("show", show);
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

        Project project = projectService.findById(issue.getProject().getId());

        UserProjectRoles userProjectRoles = getUserProjectRoles(project, request.getUserPrincipal());

        if(isAdmin(request) || userProjectRoles.isGuest()){

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

        UserProjectRoles userProjectRoles = getUserProjectRoles(project, principal);

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
                                  BindingResult theBindingResult, HttpServletRequest request) {

        // form validation
        if (theBindingResult.hasErrors()) return "dashboard/issue-form";


        Project project = projectService.findById(formIssue.getProjectId());

        if(project == null) return "redirect:/dashboard/projects";

        UserProjectRoles userProjectRoles = getUserProjectRoles(project, request.getUserPrincipal());

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

    private UserProjectRoles getUserProjectRoles(Project project, Principal principal) {

        User currentUser = userService.findByUsername(principal.getName());

        UserProjectRoles userProjectRoles = new UserProjectRoles();

        userProjectRoles.setGuest(project.getGuestUsers().contains(currentUser));
        userProjectRoles.setCollaborator(project.getCollaborators().contains(currentUser));
        userProjectRoles.setOwner(project.getCreatedBy().equals(currentUser));

        return userProjectRoles;
    }
}
