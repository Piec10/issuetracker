package com.piec10.issuetracker.form;

import com.piec10.issuetracker.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

public class FormProject {

    private int id;

    @NotNull(message = "Title is required")
    @Size(min = 1, message = "Title is required")
    private String title;

    private String description;

    private Collection<User> guestUsers;

    private Collection<User> collaborators;

    public FormProject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<User> getGuestUsers() {
        return guestUsers;
    }

    public void setGuestUsers(Collection<User> guestUsers) {
        this.guestUsers = guestUsers;
    }

    public Collection<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Collection<User> collaborators) {
        this.collaborators = collaborators;
    }
}
