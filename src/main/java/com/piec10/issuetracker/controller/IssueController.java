package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.controller.request.issue.IssueRequestFactory;
import com.piec10.issuetracker.entity.*;
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
import java.util.List;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;
import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private IssueRequestFactory issueRequestFactory;

    private Request issueRequest;

    @GetMapping("/issues")
    public String getIssues(@RequestParam(value = "projectId") int projectId,
                            @RequestParam(value = "show", required = false) String show,
                            @RequestParam(value = "sort", required = false) String sort,
                            HttpServletRequest request, Model model) {

        Project project = projectService.findById(projectId);
        if (project == null) return toProjects();
        if (isNotAdminOrProjectFollower(request, project)) return toAccessDenied();

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        UserProjectRoles userProjectRoles = getUserProjectRoles(currentUser, project);

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
        model.addAttribute("allIssueStatuses", issueService.findAllIssueStatuses());
        model.addAttribute("allIssueTypes", issueService.findAllIssueTypes());

        return "dashboard/issues";
    }

    @GetMapping("/issue")
    public String issueDetails(@RequestParam("issueId") int issueId, HttpServletRequest request, Model model) {

        issueRequest = issueRequestFactory.createIssueDetailsRequest(issueId, request, model);

        return issueRequest.processRequest();
    }

    @GetMapping("/newIssue")
    public String showNewIssueForm(@RequestParam(value = "projectId") int projectId,
                                   HttpServletRequest request, Model model) {

        issueRequest = issueRequestFactory.createNewIssueRequest(projectId, request, model);

        return issueRequest.processRequest();
    }


    @GetMapping("/editIssue")
    public String showEditIssueForm(@RequestParam("issueId") int issueId, HttpServletRequest request, Model model) {

        issueRequest = issueRequestFactory.createEditIssueRequest(issueId, request, model);

        return issueRequest.processRequest();
    }

    @PostMapping("/processIssue")
    public String processIssue(@Valid @ModelAttribute("formIssue") FormIssue formIssue,
                               BindingResult bindingResult, HttpServletRequest request, Model model) {

        issueRequest = issueRequestFactory.createProcessIssueRequest(formIssue, bindingResult, request, model);

        return issueRequest.processRequest();
    }

    @DeleteMapping("/deleteIssue/{issueId}")
    public String deleteIssue(@PathVariable int issueId, HttpServletRequest request) {

        issueRequest = issueRequestFactory.createDeleteIssueRequest(issueId, request);

        return issueRequest.processRequest();
    }

    @PatchMapping("/closeIssue/{issueId}")
    public String closeIssue(@PathVariable int issueId, HttpServletRequest request) {

        issueRequest = issueRequestFactory.createCloseIssueRequest(issueId, request);

        return issueRequest.processRequest();
    }

    @PatchMapping("/reopenIssue/{issueId}")
    public String reopenIssue(@PathVariable int issueId, HttpServletRequest request) {

        issueRequest = issueRequestFactory.createReopenIssueRequest(issueId, request);

        return issueRequest.processRequest();
    }

    @PatchMapping("/changeIssueStatus/{issueId}")
    public String changeIssueStatus(@PathVariable int issueId,
                                    @RequestParam("statusId") int statusId,
                                    HttpServletRequest request) {

        issueRequest = issueRequestFactory.createChangeStatusIssueRequest(issueId, request, statusId);

        return issueRequest.processRequest();
    }

    @PatchMapping("/changeIssueType/{issueId}")
    public String changeIssueType(@PathVariable int issueId,
                                    @RequestParam("typeId") int typeId,
                                    HttpServletRequest request) {

        issueRequest = issueRequestFactory.createChangeTypeIssueRequest(issueId, request, typeId);

        return issueRequest.processRequest();
    }

    private List<Issue> getIssues(int projectId, String show, String sort) {

        if (show.equals("open")) {

            if (sort.equals("noop")) return issueService.findOpen(projectId);

            if (sort.equals("priorityAsc")) return issueService.findOpenPriorityAsc(projectId);

            if (sort.equals("priorityDesc")) return issueService.findOpenPriorityDesc(projectId);
        }

        if (show.equals("closed")) {

            if (sort.equals("noop")) return issueService.findClosed(projectId);

            if (sort.equals("priorityAsc")) return issueService.findClosedPriorityAsc(projectId);

            if (sort.equals("priorityDesc")) return issueService.findClosedPriorityDesc(projectId);
        }

        if (show.equals("all")) {

            if (sort.equals("noop")) return issueService.findAll(projectId);

            if (sort.equals("priorityAsc")) return issueService.findAllPriorityAsc(projectId);

            if (sort.equals("priorityDesc")) return issueService.findAllPriorityDesc(projectId);
        }

        return null;
    }

    private boolean isNotAdminOrProjectFollower(HttpServletRequest request, Project project) {
        return !isAdminOrProjectFollower(request, project);
    }

    private boolean isAdminOrProjectFollower(HttpServletRequest request, Project project) {
        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        return isAdmin(request) || isProjectFollower(currentUser, project);
    }

    private static String toProjects() {
        return "redirect:/dashboard/projects";
    }

    private static String toAccessDenied() {
        return "redirect:/access-denied";
    }
}
