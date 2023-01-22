package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import com.piec10.issuetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.piec10.issuetracker.controller.util.MockRequestUsers.admin;
import static com.piec10.issuetracker.controller.util.MockRequestUsers.user;

@Import(SecurityConfig.class)
@WebMvcTest(AdminPanelController.class)
public class AdminPanelControllerTest extends BaseControllerTest {

    private final String adminPanelUrl = "/dashboard/adminPanel/";

    @MockBean
    private UserService userService;

    @Test
    public void getAdminPanelAnonymousUser() throws Exception {
        givenUrl(adminPanelUrl);
        whenPerformGet();
        thenExpect3xxLoginPage();
    }

    @Test
    public void getAdminPanelIsNotAdmin() throws Exception {
        givenUrl(adminPanelUrl);
            andUser(user());
        whenPerformGet();
        thenExpect4xxClientError();
    }

    @Test
    public void getAdminPanelIsAdmin() throws Exception {
        givenUrl(adminPanelUrl);
            andUser(admin());
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/admin-panel");
            andExpectModelAttribute("users");
    }

    @Test
    public void deleteUser() throws Exception {
        givenUrl("/dashboard/adminPanel/deleteUser/{userId}", "user");
            andUser(admin());
        whenPerformDelete();
        thenExpect3xxRedirectionTo("/dashboard/adminPanel/");
            andExpectMethodCalledOnceIn(userService).deleteById("user");
    }
}
