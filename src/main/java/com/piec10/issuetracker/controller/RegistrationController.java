package com.piec10.issuetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping("/showRegistrationForm")
    public String showRegistrationPage(Model theModel) {

        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(Model theModel) {

        return "home";
    }
}
