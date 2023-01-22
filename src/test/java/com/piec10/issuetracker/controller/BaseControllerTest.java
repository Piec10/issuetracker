package com.piec10.issuetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public abstract class BaseControllerTest {

    private final String loginUrlPattern = "http://*/login";
    private final String accessDeniedUrl = "/access-denied";

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions resultActions;

    private String url;

    private Object[] uriVariables;

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser;

    private MockHttpServletRequestBuilder request;

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

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
        performRequest(HttpMethod.GET);
    }

    protected void whenPerformPost() throws Exception {
        performRequest(HttpMethod.POST);
    }

    protected void whenPerformDelete() throws Exception {
        performRequest(HttpMethod.DELETE);
    }

    private void performRequest(HttpMethod method) throws Exception{
        request = request(method, url, uriVariables);

        if(requestUser != null){
            request.with(requestUser);
        }
        if(method == HttpMethod.POST || method == HttpMethod.DELETE) {
            request.with(csrf());
        }
        request.params(params);

        resultActions = mockMvc.perform(request);
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

    protected <T extends Object> T andExpectMethodCalledOnceIn(T service) {
        return verify(service);
    }
}
