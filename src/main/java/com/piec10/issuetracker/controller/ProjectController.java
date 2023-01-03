package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormProject;
import com.piec10.issuetracker.service.ProjectService;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class ProjectController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public String getProjects(Model model, HttpServletRequest request) {

        Collection<Project> projects;

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        if (isAdmin(request)) projects = projectService.findAll();
        else projects = currentUser.getFollowedProjects();

        model.addAttribute("projects", projects);
        model.addAttribute("user", currentUser);

        return "dashboard/projects";
    }

    @GetMapping("/newProject")
    public String showNewProjectForm(Model model, HttpServletRequest request) {

        if (isNotGuest(request)) {
            model.addAttribute("formProject", new FormProject());
            return "dashboard/project-form";
        } else return "redirect:/access-denied";
    }

    @GetMapping("/editProject")
    public String showEditProjectForm(@RequestParam("projectId") int projectId, Model model, HttpServletRequest request) {

        Project project = projectService.findById(projectId);

        if (project == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(project.getCreatedBy(), request)) {

            FormProject formProject = new FormProject();

            formProject.setId(projectId);
            formProject.setTitle(project.getTitle());
            formProject.setDescription(project.getDescription());
            formProject.setFollowers(project.getFollowers());
            formProject.setCollaborators(project.getCollaborators());

            model.addAttribute("formProject", formProject);

            return "dashboard/project-form";
        } else return "redirect:/access-denied";
    }

    @PostMapping("/processProject")
    public String processProject(@Valid @ModelAttribute("formProject") FormProject formProject,
                                 BindingResult theBindingResult, HttpServletRequest request) {

        // form validation
        if (theBindingResult.hasErrors()) return "dashboard/project-form";

        if (isGuest(request)) return "redirect:/access-denied";

        if (formProject.getId() == 0) {

            User createdBy = userService.findByUsername(request.getUserPrincipal().getName());
            projectService.createProject(formProject, createdBy);
            return "redirect:/dashboard/projects";
        }

        Project project = projectService.findById(formProject.getId());

        if (project == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(project.getCreatedBy(), request)) {

            projectService.updateProject(formProject);
            return "redirect:/dashboard/projects";
        }
        else return "redirect:/access-denied";
    }

    @DeleteMapping("/deleteProject/{projectId}")
    public String deleteProject(@PathVariable int projectId, HttpServletRequest request) {

        Project project = projectService.findById(projectId);

        if (project == null) return "redirect:/dashboard/projects";

        if (isAdminOrOwner(project.getCreatedBy(), request)) {

            projectService.deleteById(projectId);
            return "redirect:/dashboard/projects";
        }
        else return "redirect:/access-denied";
    }
}
