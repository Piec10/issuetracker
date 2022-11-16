package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.user.FormUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

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
//        User existing = userService.findByUserName(userName);
//        if (existing != null){
//            theModel.addAttribute("crmUser", new CrmUser());
//            theModel.addAttribute("registrationError", "User name already exists.");
//
//            logger.warning("User name already exists.");
//            return "registration-form";
//        }
//
//        // create user account
//        userService.save(formUser);
//
//        logger.info("Successfully created user: " + userName);

        return "registration-confirmation";
    }
}
