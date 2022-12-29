package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.dao.RoleRepository;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.Role;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.logging.Logger;

@Controller
public class MainController {

    @GetMapping("/home")
    public String getHome(){
        return "home";
    }

    @GetMapping("/contact")
    public String getContact(){
        return "contact";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }

    @GetMapping("test")
    public String getTest(Model model){

        User testUser = new User();

        testUser.setUsername("test user");


        Collection<Project> projects = new ArrayList<>();

        Project project = new Project();

        project.setId(1);
        project.setTitle("very long test project title ");
        project.setDescription("description description description description description description description description description description description description");
        project.setCreatedBy(testUser);
        project.setGuestUsers(Arrays.asList(testUser));
        project.setCollaborators(Arrays.asList(testUser));
        project.setIssues(new ArrayList<>());
        project.setCreatedAt(new Date());

        Project project2 = new Project();

        project2.setId(2);
        project2.setTitle("very long test project 2 title ");
        project2.setDescription("description description description description description description description description description description description description");
        project2.setCreatedBy(testUser);
        project2.setGuestUsers(Arrays.asList(testUser));
        project2.setCollaborators(Arrays.asList(testUser));
        project2.setIssues(new ArrayList<>());
        project2.setCreatedAt(new Date());

        projects.add(project);
        projects.add(project2);

        testUser.setGuestProjects(projects);
        testUser.setCollaborationProjects(projects);

        model.addAttribute("projects", projects);
        model.addAttribute("user", testUser);
        return "test";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}
