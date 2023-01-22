package com.piec10.issuetracker.controller.util;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public abstract class MockUserService {

    private static User user = new User("user");
    private static User guest = new User("guest");
    private static User owner = new User("owner");
    private static User admin = new User("admin");
    private static User collaborator = new User("collaborator");
    private static User follower = new User("follower");
    private static User notFollower = new User("notFollower");
    private static User projectOwner = new User("projectOwner");

    private MockUserService() {
    }

    public static void mockSetup(UserService userService) {
        when(userService.findAll()).thenReturn(new ArrayList<>());
        when(userService.findByUsername("user")).thenReturn(user);
        when(userService.findByUsername("guest")).thenReturn(guest);
        when(userService.findByUsername("owner")).thenReturn(owner);
        when(userService.findByUsername("admin")).thenReturn(admin);
        when(userService.findByUsername("collaborator")).thenReturn(collaborator);
        when(userService.findByUsername("follower")).thenReturn(follower);
        when(userService.findByUsername("notFollower")).thenReturn(notFollower);
        when(userService.findByUsername("projectOwner")).thenReturn(projectOwner);
    }

    public static User getOwner() {
        return owner;
    }

    public static User getCollaborator() {
        return collaborator;
    }

    public static User getFollower() {
        return follower;
    }

    public static User getProjectOwner() {
        return projectOwner;
    }

    public static User getAdmin() {
        return admin;
    }
}
