package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class ProjectController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public String getProjects(Principal principal, Model model) {

        User currentUser =  userService.findByUsername(principal.getName());

        model.addAttribute("user", currentUser);

        return "dashboard/projects";
    }

    @GetMapping("/newProject")
    public String showNewProjectForm(Model model, HttpServletRequest request) {

        if(isNotGuest(request))
        {
            model.addAttribute("formProject", new FormProject());
            return "dashboard/project-form";
        }
        else return "redirect:/access-denied";
    }

    @PostMapping("/processProject")
    public String processProject(@Valid @ModelAttribute("formProject") FormProject formProject,
                                 BindingResult theBindingResult, HttpServletRequest request) {

        // form validation
        if (theBindingResult.hasErrors()) return "dashboard/project-form";

        if(isNotGuest(request)) {

            if(formProject.getId() == 0){

                User createdBy = userService.findByUsername(request.getUserPrincipal().getName());
                projectService.createProject(formProject, createdBy);
            }
            else{

            }
            return "redirect:/dashboard/projects";
        }
        else return "redirect:/access-denied";
    }

    private boolean isNotGuest(HttpServletRequest request) {
        return !request.isUserInRole("ROLE_GUEST");
    }
}
