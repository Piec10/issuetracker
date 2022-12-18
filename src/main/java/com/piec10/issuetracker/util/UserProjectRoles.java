package com.piec10.issuetracker.util;

public class UserProjectRoles {

    private boolean isGuest;

    private boolean isCollaborator;

    private boolean isOwner;

    public UserProjectRoles() {
    }

    public UserProjectRoles(boolean isGuest, boolean isCollaborator, boolean isOwner) {
        this.isGuest = isGuest;
        this.isCollaborator = isCollaborator;
        this.isOwner = isOwner;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public boolean isCollaborator() {
        return isCollaborator;
    }

    public void setCollaborator(boolean collaborator) {
        isCollaborator = collaborator;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
