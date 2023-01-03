package com.piec10.issuetracker.util;

public class UserProjectRoles {

    private boolean isFollower;

    private boolean isCollaborator;

    private boolean isOwner;

    public UserProjectRoles() {
    }

    public UserProjectRoles(boolean isFollower, boolean isCollaborator, boolean isOwner) {
        this.isFollower = isFollower;
        this.isCollaborator = isCollaborator;
        this.isOwner = isOwner;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
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
