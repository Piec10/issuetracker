package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(AdminPanelController.class)
public class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void getDashboardAnonymousUser() throws Exception {

        mockMvc.perform(get("/dashboard/adminPanel/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://*/login"));
    }

    @Test
    public void getDashboardIsNotAdmin() throws Exception {

        mockMvc.perform(get("/dashboard/adminPanel/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getDashboardIsAdmin() throws Exception {

        when(userService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/adminPanel/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/admin-panel"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/dashboard/adminPanel/deleteUser/{userId}", "user")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER", "ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/dashboard/adminPanel/"));

        verify(userService, times(1)).deleteById("user");
    }
}
