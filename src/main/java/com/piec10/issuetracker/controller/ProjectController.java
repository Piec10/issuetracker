package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.controller.request.project.ProjectRequestFactory;
import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.dto.ProjectDtoWrapper;
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
import java.util.stream.Collectors;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDtoWrapper projectDtoWrapper;

    @Autowired
    private ProjectRequestFactory projectRequestFactory;

    private Request projectRequest;


//    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/projects")
    public String getProjects(HttpServletRequest request, Model model) {

        Collection<Project> projects;

        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        if (isAdmin(request)) projects = projectService.findAll();
        else projects = currentUser.getFollowedProjects();

        model.addAttribute("projects",
                projects.stream().map(project -> projectDtoWrapper.wrap(project)).collect(Collectors.toList()));
        model.addAttribute("user", currentUser);

        return "dashboard/projects";
    }

    @GetMapping("/newProject")
    public String showNewProjectForm(Model model) {

        model.addAttribute("formProject", new FormProject());
        return "dashboard/project-form";
    }

    @GetMapping("/editProject")
    public String showEditProjectForm(@RequestParam("projectId") int projectId, HttpServletRequest request, Model model) {

        projectRequest = projectRequestFactory.createEditProjectRequest(projectId, request, model);

        return projectRequest.processRequest();
    }

    @PostMapping(value = "/processProject", params = "action=search")
    public String processProjectSearch(@ModelAttribute("formProject") FormProject formProject) {

        formProject.getSearchResults().clear();

        User searchedUser = userService.findByUsername(formProject.getSearchedUsername());

        if (searchedUser != null) formProject.getSearchResults().add(searchedUser.getUsername());

        return "dashboard/project-form";
    }

    @PostMapping(value = "/processProject", params = "action=addCollaborator")
    public String processProjectAddCollaborator(@ModelAttribute("formProject") FormProject formProject,
                                                @RequestParam("username") String collaboratorUsername) {

        if (!formProject.getCollaboratorsNames().contains(collaboratorUsername))
            formProject.getCollaboratorsNames().add(collaboratorUsername);

        if (!formProject.getFollowersNames().contains(collaboratorUsername))
            formProject.getFollowersNames().add(collaboratorUsername);

        return "dashboard/project-form";
    }

    @PostMapping(value = "/processProject", params = "action=addFollower")
    public String processProjectAddFollower(@ModelAttribute("formProject") FormProject formProject,
                                            @RequestParam("username") String followerUsername) {

        if (formProject.getCollaboratorsNames().contains(followerUsername))
            formProject.getCollaboratorsNames().remove(followerUsername);

        if (!formProject.getFollowersNames().contains(followerUsername))
            formProject.getFollowersNames().add(followerUsername);

        return "dashboard/project-form";
    }

    @PostMapping(value = "/processProject", params = "action=removeFollower")
    public String processProjectRemoveFollower(@ModelAttribute("formProject") FormProject formProject,
                                               @RequestParam("username") String followerUsername) {

        if (formProject.getCollaboratorsNames().contains(followerUsername))
            formProject.getCollaboratorsNames().remove(followerUsername);

        if (formProject.getFollowersNames().contains(followerUsername))
            formProject.getFollowersNames().remove(followerUsername);

        return "dashboard/project-form";
    }

    @PostMapping(value = "/processProject", params = "action=removeCollaborator")
    public String processProjectRemoveCollaborator(@ModelAttribute("formProject") FormProject formProject,
                                                   @RequestParam("username") String collaboratorUsername) {

        if (formProject.getCollaboratorsNames().contains(collaboratorUsername))
            formProject.getCollaboratorsNames().remove(collaboratorUsername);

        return "dashboard/project-form";
    }

    @PostMapping("/processProject")
    public String processProject(@Valid @ModelAttribute("formProject") FormProject formProject,
                                 BindingResult theBindingResult, HttpServletRequest request, Model model) {

        // form validation
        if (theBindingResult.hasErrors()) return "dashboard/project-form";

        if (isGuest(request)) {

            model.addAttribute("guestUserError", "Sorry, Guest user cannot create new projects.");
            return "dashboard/project-form";
        }

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
        } else return "redirect:/access-denied";
    }

    @DeleteMapping("/deleteProject/{projectId}")
    public String deleteProject(@PathVariable int projectId, HttpServletRequest request) {

        projectRequest = projectRequestFactory.createDeleteProjectRequest(projectId, request);

        return projectRequest.processRequest();
    }
}
