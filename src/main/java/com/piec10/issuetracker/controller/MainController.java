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

//    @GetMapping("test")
//    public String getTest(Model model){
//
//        return "test";
//    }

    @GetMapping("/login")
    public String showMyLoginPage() {

        return "login";

    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}
