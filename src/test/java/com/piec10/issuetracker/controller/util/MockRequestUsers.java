package com.piec10.issuetracker.controller.util;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class MockRequestUsers {

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin() {
        return SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN");
    }

    public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user() {
        return SecurityMockMvcRequestPostProcessors.user("user").roles("USER");
    }
}
