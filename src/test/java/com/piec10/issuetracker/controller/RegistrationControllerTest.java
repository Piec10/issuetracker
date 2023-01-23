package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.controller.util.MockUserService;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormUser;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import(SecurityConfig.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest extends BaseControllerTest{

    @MockBean
    private UserService userService;

    @PostConstruct
    public void setup() {
        MockUserService.mockSetup(userService);
        when(userService.exists("user")).thenReturn(true);
        when(userService.findByEmail("email@email.com")).thenReturn(new User());
    }

    @Test
    public void getRegistrationFormAnonymousUser() throws Exception {

        mockMvc.perform(get("/register/showRegistrationForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().attributeExists("formUser"));
    }

    @Test
    public void processRegistrationFormHasErrorsPasswordMismatch() throws Exception {

        mockMvc.perform(post("/register/processRegistrationForm")
                        .param("username", "user")
                        .param("email", "email@email.com")
                        .param("password", "password")
                        .param("matchingPassword", "wrongPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("formUser"));
    }

    @Test
    public void processRegistrationFormHasErrorsWrongEmail() throws Exception {

        mockMvc.perform(post("/register/processRegistrationForm")
                        .param("username", "user")
                        .param("email", "email")
                        .param("password", "password")
                        .param("matchingPassword", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("formUser"));
    }

    @Test
    public void processRegistrationFormUserAlreadyExist() throws Exception {

        mockMvc.perform(post("/register/processRegistrationForm")
                        .param("username", "user")
                        .param("email", "email@email.com")
                        .param("password", "password")
                        .param("matchingPassword", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().attributeExists("registrationError"));
    }

    @Test
    public void processRegistrationFormEmailAlreadyExist() throws Exception {



        mockMvc.perform(post("/register/processRegistrationForm")
                        .param("username", "user2")
                        .param("email", "email@email.com")
                        .param("password", "password")
                        .param("matchingPassword", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().attributeExists("registrationError"));
    }

    @Test
    public void processRegistrationFormCreateValidUser() throws Exception {

        FormUser formUser = new FormUser();

        mockMvc.perform(post("/register/processRegistrationForm")
                        .param("username", "user2")
                        .param("email", "email2@email.com")
                        .param("password", "password")
                        .param("matchingPassword", "password")
                        .flashAttr("formUser", formUser)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-confirmation"));

        verify(userService, times(1)).save(formUser);
    }
}
