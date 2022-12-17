package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import com.piec10.issuetracker.form.FormUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/showRegistrationForm")
    public String showRegistrationPage(Model theModel) {

        theModel.addAttribute("formUser", new FormUser());
        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("formUser") FormUser formUser,
            BindingResult theBindingResult,
            Model theModel) {

        // form validation
        if (theBindingResult.hasErrors()){
            return "registration-form";
        }

        // check the database if user already exists
        if(userService.exists(formUser.getUsername())){
            theModel.addAttribute("registrationError", "User already exists.");

            return "registration-form";
        }

        User existing = userService.findByEmail(formUser.getEmail());
        if(existing != null){
            theModel.addAttribute("registrationError", "Email already exists.");

            return "registration-form";
        }

        // create user account
        userService.save(formUser);

        return "registration-confirmation";
    }
}
