package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static User user;

    @BeforeAll
    public static void beforeAll() {
        user = new User();
        user.setUsername("owner");
    }

    @Test
    public void getDashboardAnonymousUser() throws Exception {

        mockMvc.perform(get("/dashboard/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @Test
    public void getDashboardAuthenticatedUser() throws Exception {

        mockMvc.perform(get("/dashboard/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getProfileAuthenticatedUser() throws Exception {

        when(userService.findByUsername("user")).thenReturn(new User());

        mockMvc.perform(get("/dashboard/profile")
                    .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void getPasswordChangePageGuestUser() throws Exception {

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","user")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER", "GUEST")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getPasswordChangePageInvalidUserId() throws Exception {

        when(userService.findByUsername("invalidUser")).thenReturn(null);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","invalidUser")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void getPasswordChangePageValidUserIdIsOwner() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(user);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("formPassword"));
    }

    @Test
    public void getPasswordChangePageValidUserIdIsNotOwner() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(user);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("notOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getPasswordChangePageValidUserIdIsAdmin() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(user);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("formPassword"));
    }
}
