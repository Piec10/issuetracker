package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static com.piec10.issuetracker.controller.util.MockRequestUsers.admin;
import static com.piec10.issuetracker.controller.util.MockRequestUsers.user;
import static org.mockito.Mockito.*;

@Import(SecurityConfig.class)
@WebMvcTest(AdminPanelController.class)
public class AdminPanelControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    @PostConstruct
    private void mockSetup() {

        when(userService.findAll()).thenReturn(new ArrayList<>());
    }

    @Test
    public void getAdminPanelAnonymousUser() throws Exception {

        whenPerformGetAsAnonymous("/dashboard/adminPanel/");
        thenExpect3xxRedirectionToPattern("http://*/login");
    }

    @Test
    public void getAdminPanelIsNotAdmin() throws Exception {

        whenPerformGetAs(user(), "/dashboard/adminPanel/");
        thenExpect4xxClientError();
    }

    @Test
    public void getAdminPanelIsAdmin() throws Exception {

        whenPerformGetAs(admin(), "/dashboard/adminPanel/");
        thenExpectIsOkAndView("dashboard/admin-panel");
        expectModelAttribute("users");
    }

    @Test
    public void deleteUser() throws Exception {

        whenPerformDeleteAs(admin(),"/dashboard/adminPanel/deleteUser/{userId}", "user");
        thenExpect3xxRedirectionTo("/dashboard/adminPanel/");

        verify(userService).deleteById("user");
    }

}
