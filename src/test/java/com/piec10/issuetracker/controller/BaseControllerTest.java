package com.piec10.issuetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    protected ResultActions resultActions;

    protected void whenPerformGetAsAnonymous(String url) throws Exception {
        resultActions = mockMvc.perform(get(url));
    }

    protected void whenPerformGetAs(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser,
                                    String url) throws Exception {
        resultActions = mockMvc.perform(get(url)
                        .with(requestUser));
    }
    protected void whenPerformDeleteAs(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor requestUser,
                                       String url, Object... uriVariables) throws Exception {
        resultActions = mockMvc.perform(delete(url, uriVariables)
                        .with(csrf())
                        .with(requestUser));
    }

    public void thenExpect3xxRedirectionToPattern(String urlPattern) throws Exception {
        resultActions.andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern(urlPattern));
    }

    protected void thenExpect3xxRedirectionTo(String location) throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", location));
    }

    protected void thenExpect4xxClientError() throws Exception{
        resultActions.andExpect(status().is4xxClientError());
    }

    protected void thenExpectIsOkAndView(String expectedViewName) throws Exception  {
        resultActions.andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));
    }

    protected void expectModelAttribute(String attribute) throws Exception {
        resultActions.andExpect(model().attributeExists(attribute));
    }
}
