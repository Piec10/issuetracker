package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BaseControllerTest {

    private final String loginUrlPattern = "http://*/login";
    private final String accessDeniedUrl = "/access-denied";

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions resultActions;

    @MockBean
    protected UserService userService;

    private String url;

    private Object[] uriVariables;

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser;

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    private User user = new User("user");

    private User guest = new User("guest");

    private User owner = new User("owner");

    @PostConstruct
    private void mockSetup() {
        when(userService.findAll()).thenReturn(new ArrayList<>());
        when(userService.findByUsername("user")).thenReturn(user);
        when(userService.findByUsername("guest")).thenReturn(guest);
        when(userService.findByUsername("owner")).thenReturn(owner);
    }

    protected void givenUrl(String url, Object... uriVariables) {
        givenUrl(url);
        this.uriVariables = uriVariables;
    }

    protected void givenUrl(String url) {
        andUrl(url);
        requestUser = null;
        params.clear();
        uriVariables = new Object[]{};
    }

    protected void andUrl(String url, Object... uriVariables) {
        andUrl(url);
        this.uriVariables = uriVariables;
    }

    protected void andUrl(String url) {
        this.url = url;
    }

    protected void givenUser(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser) {
        andUser(requestUser);
        params.clear();
        uriVariables = new Object[]{};
    }

    protected void andUser(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser) {
        this.requestUser = requestUser;
    }

    protected void givenParam(String name, String value) {
        requestUser = null;
        params.clear();
        uriVariables = new Object[]{};
        andParam(name, value);
    }

    protected void andParam(String name, String value) {
        params.put(name, List.of(value));
    }

    protected void whenPerformGet() throws Exception {
        if(requestUser == null)
            resultActions = mockMvc.perform(get(url, uriVariables).params(params));
        else
            resultActions = mockMvc.perform(get(url, uriVariables).params(params).with(requestUser));
    }

    protected void whenPerformPost() throws Exception {
        if(requestUser == null)
            resultActions = mockMvc.perform(post(url, uriVariables).params(params).with(csrf()));
        else
            resultActions = mockMvc.perform(post(url, uriVariables).params(params).with(requestUser).with(csrf()));
    }

    protected void whenPerformDelete() throws Exception {
        if(requestUser == null)
            resultActions = mockMvc.perform(delete(url, uriVariables).params(params).with(csrf()));
        else
            resultActions = mockMvc.perform(delete(url, uriVariables).params(params).with(requestUser).with(csrf()));
    }

    protected void thenExpect3xxRedirectionTo(String location) throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", location));
    }

    protected void thenExpect3xxLoginPage() throws Exception {
        resultActions.andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern(loginUrlPattern));
    }

    protected void thenExpect3xxAccessDenied() throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", accessDeniedUrl));
    }

    protected void thenExpect4xxClientError() throws Exception{
        resultActions.andExpect(status().is4xxClientError());
    }

    protected void thenExpectIsOkAndView(String expectedViewName) throws Exception  {
        resultActions.andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));
    }

    protected void andExpectModelAttribute(String attribute) throws Exception {
        resultActions.andExpect(model().attributeExists(attribute));
    }

    protected void andExpectModelErrors() throws Exception {
        resultActions.andExpect(model().hasErrors());
    }

    protected void andExpectModelAttributeHasErrors(String attribute) throws Exception {
        resultActions.andExpect(model().attributeHasErrors(attribute));
    }

    protected User getOwner() {
        return owner;
    }
}
