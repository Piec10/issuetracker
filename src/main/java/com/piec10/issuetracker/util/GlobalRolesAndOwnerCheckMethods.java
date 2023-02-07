package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.HasCreator;
import com.piec10.issuetracker.entity.Issue;
import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.isProjectFollower;

public abstract class GlobalRolesAndOwnerCheckMethods {

    public static boolean doesNotHavePermissionToModify(Issue issue, HttpServletRequest request) {
        return !hasPermissionToModify(issue, request);
    }

    public static boolean hasPermissionToModify(Issue issue, HttpServletRequest request) {
        return isOwner(issue.getProject(), request) || isOwner(issue, request) || isAdmin(request);
    }

    public static boolean isNotAdminOrProjectFollower(User currentUser, Project project) {
        return !isAdminOrProjectFollower(currentUser, project);
    }

    public static boolean isAdminOrProjectFollower(User user, Project project) {
        return isAdmin(user) || isProjectFollower(user, project);
    }

    public static boolean isNotAdminOrOwner(HasCreator object, HttpServletRequest request) {
        return !isAdminOrOwner(object, request);
    }

    public static boolean isAdminOrOwner(HasCreator object, HttpServletRequest request) {
        return isAdmin(request) || isOwner(object, request);
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }

    public static boolean isAdmin(User user) {
        return user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()).contains("ROLE_ADMIN");
    }

    public static boolean isOwner(HasCreator object, HttpServletRequest request) {
        return ((object != null && object.getCreatedBy() != null) ?
                (object.getCreatedBy().getUsername().equals(request.getUserPrincipal().getName())) : false);
    }

    public static boolean isGuest(HttpServletRequest request) {
        return request.isUserInRole("ROLE_GUEST");
    }

    public static boolean isNotGuest(HttpServletRequest request) {
        return !request.isUserInRole("ROLE_GUEST");
    }
}
