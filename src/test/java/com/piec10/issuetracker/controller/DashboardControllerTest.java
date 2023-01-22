package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

import static com.piec10.issuetracker.controller.util.MockRequestUsers.*;

@Import(SecurityConfig.class)
@WebMvcTest(DashboardController.class)
public class DashboardControllerTest extends BaseControllerTest {

    private final String dashboardUrl = "/dashboard/";
    private final String changePasswordUrl = "/dashboard/changePassword";
    private final String processPasswordChangeUrl = "/dashboard/processPasswordChange";

    @PostConstruct
    public void setup() {
        getOwner().setPassword(new BCryptPasswordEncoder().encode("oldPass"));
    }

    @Test
    public void getDashboardAnonymousUser() throws Exception {
        givenUrl(dashboardUrl);
        whenPerformGet();
        thenExpect3xxLoginPage();
    }

    @Test
    public void getDashboardAuthenticatedUser() throws Exception {
        givenUrl(dashboardUrl);
            andUser(user());
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getProfileAuthenticatedUser() throws Exception {
        givenUrl("/dashboard/profile");
            andUser(user());
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/profile");
            andExpectModelAttribute("user");
    }

    @Test
    public void getPasswordChangePageIsGuestUser() throws Exception {
        givenUrl(changePasswordUrl);
            andUser(guest());
            andParam("userId", "guest");
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/password-change");
            andExpectModelAttribute("formPassword");
    }

    @Test
    public void getPasswordChangePageInvalidUserId() throws Exception {
        givenUrl(changePasswordUrl);
            andUser(user());
            andParam("userId","invalidUser");
        whenPerformGet();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void getPasswordChangePageIsOwnerValidUserId() throws Exception {
        givenUrl(changePasswordUrl);
            andUser(owner());
            andParam("userId","owner");
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/password-change");
            andExpectModelAttribute("formPassword");
    }

    @Test
    public void getPasswordChangePageIsNotOwnerValidUserId() throws Exception {
        givenUrl(changePasswordUrl);
            andUser(user());
            andParam("userId","owner");
        whenPerformGet();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void getPasswordChangePageIsAdminValidUserId() throws Exception {
        givenUrl(changePasswordUrl);
            andUser(admin());
            andParam("userId","owner");
        whenPerformGet();
        thenExpectIsOkAndView("dashboard/password-change");
            andExpectModelAttribute("formPassword");
    }

    @Test
    public void processPasswordChangeFormHasErrors() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(user());
            andParam("username", "user");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass1");
        whenPerformPost();
        thenExpectIsOkAndView("dashboard/password-change");
            andExpectModelErrors();
            andExpectModelAttributeHasErrors("formPassword");
    }

    @Test
    public void processPasswordChangeIsGuestUser() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(guest());
            andParam("username", "guest");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void processPasswordChangeInvalidFormUsername() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(user());
            andParam("username", "invalidUser");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/projects");
    }

    @Test
    public void processPasswordChangeIsOwnerValidOldPassword() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(owner());
            andParam("username", "owner");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/profile");
            andExpectUserServiceMethodCalledOnce().changePassword("owner","newPass");
    }

    @Test
    public void processPasswordChangeIsOwnerInvalidOldPassword() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(owner());
            andParam("username", "owner");
            andParam("oldPassword", "wrongPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpectIsOkAndView("dashboard/password-change");
            andExpectModelAttribute("passwordError");
    }

    @Test
    public void processPasswordChangeIsNotOwner() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(user());
            andParam("username", "owner");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpect3xxAccessDenied();
    }

    @Test
    public void processPasswordChangeIsAdmin() throws Exception {
        givenUrl(processPasswordChangeUrl);
            andUser(admin());
            andParam("username", "owner");
            andParam("oldPassword", "oldPass");
            andParam("newPassword", "newPass");
            andParam("matchingNewPassword", "newPass");
        whenPerformPost();
        thenExpect3xxRedirectionTo("/dashboard/adminPanel/");
            andExpectUserServiceMethodCalledOnce().changePassword("owner","newPass");
    }
}
