package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;

public abstract class ProjectRolesCheckMethods {

    public static boolean isNotProjectCollaborator(User user, Project project) {
        return !isProjectCollaborator(user, project);
    }

    public static boolean isProjectCollaborator(User user, Project project) {
        return project.getCollaborators().contains(user);
    }

    public static boolean isProjectFollower(User user, Project project) {
        return project.getFollowers().contains(user);
    }

    public static UserProjectRoles getUserProjectRoles(User user, Project project) {

        UserProjectRoles userProjectRoles = new UserProjectRoles();

        userProjectRoles.setFollower(project.getFollowers().contains(user));
        userProjectRoles.setCollaborator(project.getCollaborators().contains(user));
        userProjectRoles.setOwner(project.getCreatedBy().equals(user));

        return userProjectRoles;
    }
}
