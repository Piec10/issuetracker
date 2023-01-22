package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
public class MainControllerTest extends BaseControllerTest {

    @Test
    public void getHomeAnonymousUser() throws Exception {
        givenUrl("/home");
        whenPerformGet();
        thenExpectIsOkAndView("home");
    }

    @Test
    public void getContactAnonymousUser() throws Exception {
        givenUrl("/contact");
        whenPerformGet();
        thenExpectIsOkAndView("contact");
    }

    @Test
    public void getAboutAnonymousUser() throws Exception {
        givenUrl("/about");
        whenPerformGet();
        thenExpectIsOkAndView("about");
    }

    @Test
    public void getLoginAnonymousUser() throws Exception {
        givenUrl("/login");
        whenPerformGet();
        thenExpectIsOkAndView("login");
    }
}
