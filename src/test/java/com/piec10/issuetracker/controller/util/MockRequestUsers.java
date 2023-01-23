package com.piec10.issuetracker.controller.util;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public abstract class MockRequestUsers {

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin() {
        return SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user() {
        return SecurityMockMvcRequestPostProcessors.user("user").roles("USER");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor guest() {
        return SecurityMockMvcRequestPostProcessors.user("guest").roles("USER", "GUEST");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor owner() {
        return SecurityMockMvcRequestPostProcessors.user("owner").roles("USER");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor follower() {
        return SecurityMockMvcRequestPostProcessors.user("follower").roles("USER");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notFollower() {
        return SecurityMockMvcRequestPostProcessors.user("notFollower").roles("USER");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor collaborator() {
        return SecurityMockMvcRequestPostProcessors.user("collaborator").roles("USER");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor projectOwner() {
        return SecurityMockMvcRequestPostProcessors.user("projectOwner").roles("USER");
    }
}
