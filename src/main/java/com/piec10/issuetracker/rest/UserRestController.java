package com.piec10.issuetracker.rest;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

//    @GetMapping("/users")
//    public List<User> findAll(){
//
//        return userService.findAll();
//    }
}
