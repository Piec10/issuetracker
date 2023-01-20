package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static com.piec10.issuetracker.controller.util.MockRequestUsers.admin;
import static com.piec10.issuetracker.controller.util.MockRequestUsers.user;
import static org.mockito.Mockito.*;

@Import(SecurityConfig.class)
@WebMvcTest(AdminPanelController.class)
public class AdminPanelControllerTest extends BaseControllerTest {

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
        andExpectModelAttribute("users");
    }

    @Test
    public void deleteUser() throws Exception {

        whenPerformDeleteAs(admin(),"/dashboard/adminPanel/deleteUser/{userId}", "user");
        thenExpect3xxRedirectionTo("/dashboard/adminPanel/");

        verify(userService).deleteById("user");
    }

}
