package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class GlobalRolesAndOwnerCheckMethods {

    public static boolean isGuest(HttpServletRequest request) {
        return request.isUserInRole("ROLE_GUEST");
    }

    public static boolean isNotGuest(HttpServletRequest request) {
        return !request.isUserInRole("ROLE_GUEST");
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }

    public static boolean isOwner(User user, Principal principal) {
        return (user != null ? (user.getUsername().equals(principal.getName())) : false);
    }

    public static boolean isAdminOrOwner(User user, HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN") || isOwner(user, request.getUserPrincipal());
    }

    public static UserProjectRoles getUserProjectRoles(Project project, User user) {

        UserProjectRoles userProjectRoles = new UserProjectRoles();

        userProjectRoles.setFollower(project.getFollowers().contains(user));
        userProjectRoles.setCollaborator(project.getCollaborators().contains(user));
        userProjectRoles.setOwner(project.getCreatedBy().equals(user));

        return userProjectRoles;
    }
}
