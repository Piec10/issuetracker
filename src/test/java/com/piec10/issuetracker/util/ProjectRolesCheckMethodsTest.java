package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.piec10.issuetracker.util.ProjectRolesCheckMethods.getUserProjectRoles;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectRolesCheckMethodsTest {

    private static Project project;

    private static User owner;

    private static User collaborator;

    private static User follower;

    private static User notFollower;

    @BeforeAll
    public static void beforeAll() {

        project = new Project();
        owner = new User();
        collaborator = new User();
        follower = new User();
        notFollower = new User();

        project.setCreatedBy(owner);
        project.setCollaborators(Arrays.asList(owner, collaborator));
        project.setFollowers(Arrays.asList(owner, collaborator, follower));
    }

    @Test
    public void getUserProjectRolesForOwner() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(owner, project);

        assertTrue(userProjectRoles.isOwner(), "Should be project owner");
        assertTrue(userProjectRoles.isCollaborator(), "Should be project collaborator");
        assertTrue(userProjectRoles.isFollower(), "Should be project follower");
    }

    @Test
    public void getUserProjectRolesForCollaborator() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(collaborator, project);

        assertFalse(userProjectRoles.isOwner(), "Should not be project owner");
        assertTrue(userProjectRoles.isCollaborator(), "Should be project collaborator");
        assertTrue(userProjectRoles.isFollower(), "Should be project follower");
    }

    @Test
    public void getUserProjectRolesForFollower() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(follower, project);

        assertFalse(userProjectRoles.isOwner(), "Should not be project owner");
        assertFalse(userProjectRoles.isCollaborator(), "Should not be project collaborator");
        assertTrue(userProjectRoles.isFollower(), "Should be project follower");
    }

    @Test
    public void getUserProjectRolesForNotFollower() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(notFollower, project);

        assertFalse(userProjectRoles.isOwner(), "Should not be project owner");
        assertFalse(userProjectRoles.isCollaborator(), "Should not be project collaborator");
        assertFalse(userProjectRoles.isFollower(), "Should not be project follower");
    }
}
