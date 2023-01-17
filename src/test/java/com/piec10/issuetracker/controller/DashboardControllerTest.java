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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static User owner;

    @BeforeAll
    public static void beforeAll() {
        owner = new User();
        owner.setUsername("owner");
        owner.setPassword(new BCryptPasswordEncoder().encode("oldPass"));
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
    public void getPasswordChangePageIsGuestUser() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER", "GUEST")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("formPassword"));
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
    public void getPasswordChangePageIsOwnerValidUserId() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("formPassword"));
    }

    @Test
    public void getPasswordChangePageIsNotOwnerValidUserId() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("notOwner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void getPasswordChangePageIsAdminValidUserId() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(get("/dashboard/changePassword")
                        .param("userId","owner")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("formPassword"));
    }

    @Test
    public void processPasswordChangeFormHasErrors() throws Exception {

        mockMvc.perform(post("/dashboard/processPasswordChange")
                        .param("username", "user")
                        .param("oldPassword", "oldPass")
                        .param("newPassword", "newPass")
                        .param("matchingNewPassword", "newPass1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("formPassword"));
    }

    @Test
    public void processPasswordChangeIsGuestUser() throws Exception {

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "user")
                .param("oldPassword", "oldPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER", "GUEST")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processPasswordChangeInvalidFormUsername() throws Exception {

        when(userService.findByUsername("invalidUser")).thenReturn(null);

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "invalidUser")
                .param("oldPassword", "oldPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/projects"));
    }

    @Test
    public void processPasswordChangeIsOwnerValidOldPassword() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "owner")
                .param("oldPassword", "oldPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/profile"));

        verify(userService, times(1)).changePassword("owner","newPass");
    }

    @Test
    public void processPasswordChangeIsOwnerInvalidOldPassword() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "owner")
                .param("oldPassword", "wrongPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/password-change"))
                .andExpect(model().attributeExists("passwordError"));
    }

    @Test
    public void processPasswordChangeIsNotOwner() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "owner")
                .param("oldPassword", "oldPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/access-denied"));
    }

    @Test
    public void processPasswordChangeIsAdmin() throws Exception {

        when(userService.findByUsername("owner")).thenReturn(owner);

        mockMvc.perform(post("/dashboard/processPasswordChange")
                .param("username", "owner")
                .param("oldPassword", "oldPass")
                .param("newPassword", "newPass")
                .param("matchingNewPassword", "newPass")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/adminPanel/"));

        verify(userService, times(1)).changePassword("owner","newPass");
    }
}
