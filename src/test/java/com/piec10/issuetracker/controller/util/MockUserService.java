package com.piec10.issuetracker.controller.util;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class MockUserService {

    private static User user = new User("user");
    private static User guest = new User("guest");
    private static User owner = new User("owner");

    private MockUserService() {
    }

    public static void mockSetup(UserService userService) {
        when(userService.findAll()).thenReturn(new ArrayList<>());
        when(userService.findByUsername("user")).thenReturn(user);
        when(userService.findByUsername("guest")).thenReturn(guest);
        when(userService.findByUsername("owner")).thenReturn(owner);
    }

    public static User getOwner() {
        return owner;
    }
}
