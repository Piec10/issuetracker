package com.piec10.issuetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {


    //private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/")
    public String getDashboard(){

        return "redirect:/dashboard/issues";
    }

    @GetMapping("/profile")
    public String getProfile(){
        return "dashboard/profile";
    }






}
