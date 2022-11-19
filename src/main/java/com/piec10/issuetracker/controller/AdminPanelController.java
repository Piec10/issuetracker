package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/adminPanel")
public class AdminPanelController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getAdminPanel(Model model){

        List<User> users = userService.findAll();

        model.addAttribute("users",users);

        return "dashboard/admin-panel";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(){
        return null;
    }

}
