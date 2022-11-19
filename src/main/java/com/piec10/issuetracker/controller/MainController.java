package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.dao.RoleRepository;
import com.piec10.issuetracker.entity.Role;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class MainController {

//    @Autowired
//    private RoleRepository roleRepository;
//    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/home")
    public String getHome(){

//        Optional<Role> role = roleRepository.findById(1L);
//        if(role.isPresent()) logger.info(role.toString());
//
//        Role role1 = roleRepository.findByName("ROLE_USER");
//        logger.info(role1.toString());

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
}
