package com.piec10.issuetracker.util;

import com.piec10.issuetracker.entity.Project;
import com.piec10.issuetracker.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.getUserProjectRoles;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalRolesAndOwnerCheckMethodsTest {

    private static Project project;

    private static User owner;

    private static User collaborator;

    private static User guest;

    private static User notGuest;

    @BeforeAll
    public static void beforeAll() {

        project = new Project();
        owner = new User();
        collaborator = new User();
        guest = new User();
        notGuest = new User();

        project.setCreatedBy(owner);
        project.setCollaborators(Arrays.asList(owner, collaborator));
        project.setGuestUsers(Arrays.asList(owner, collaborator, guest));
    }

    @Test
    public void getUserProjectRolesForOwner() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(project,owner);

        assertTrue(userProjectRoles.isOwner(), "Should be project owner");
        assertTrue(userProjectRoles.isCollaborator(), "Should be project collaborator");
        assertTrue(userProjectRoles.isGuest(), "Should be project guest user");
    }

    @Test
    public void getUserProjectRolesForCollaborator() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(project,collaborator);

        assertFalse(userProjectRoles.isOwner(), "Should not be project owner");
        assertTrue(userProjectRoles.isCollaborator(), "Should be project collaborator");
        assertTrue(userProjectRoles.isGuest(), "Should be project guest user");
    }

    @Test
    public void getUserProjectRolesForGuest() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(project,guest);

        assertFalse(userProjectRoles.isOwner(), "Should not be project owner");
        assertFalse(userProjectRoles.isCollaborator(), "Should not be project collaborator");
        assertTrue(userProjectRoles.isGuest(), "Should be project guest user");
    }

    @Test
    public void getUserProjectRolesForNotGuest() {

        UserProjectRoles userProjectRoles = getUserProjectRoles(project,notGuest);

        assertFalse(userProjectRoles.isOwner(), "Should be project owner");
        assertFalse(userProjectRoles.isCollaborator(), "Should not be project collaborator");
        assertFalse(userProjectRoles.isGuest(), "Should not be project guest user");
    }
}
