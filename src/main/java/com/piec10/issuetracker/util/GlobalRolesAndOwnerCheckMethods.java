package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.HasCreator;

import javax.servlet.http.HttpServletRequest;

public abstract class GlobalRolesAndOwnerCheckMethods {

    public static boolean isAdminOrOwner(HasCreator object, HttpServletRequest request) {
        return isAdmin(request) || isOwner(object, request);
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
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
