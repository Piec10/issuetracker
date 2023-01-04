package com.piec10.issuetracker.form;

import com.piec10.issuetracker.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

public class FormProject {

    private int id;

    @NotNull(message = "Title is required")
    @Size(min = 1, message = "Title is required")
    private String title;

    private String description;

    private Collection<String> followersNames = new ArrayList<>();

    private Collection<String> collaboratorsNames = new ArrayList<>();

    private String searchedUsername;

    private Collection<String> searchResults = new ArrayList<>();

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

    public Collection<String> getFollowersNames() {
        return followersNames;
    }

    public void setFollowersNames(Collection<String> followersNames) {
        this.followersNames = followersNames;
    }

    public void setFollowersNamesFromUsers(Collection<User> followers){

        for(User user : followers) followersNames.add(user.getUsername());
    }

    public Collection<String> getCollaboratorsNames() {
        return collaboratorsNames;
    }

    public void setCollaboratorsNames(Collection<String> collaboratorsNames) {
        this.collaboratorsNames = collaboratorsNames;
    }

    public void setCollaboratorsNamesFromUsers(Collection<User> collaborators) {

        for(User user : collaborators) collaboratorsNames.add(user.getUsername());
    }

    public String getSearchedUsername() {
        return searchedUsername;
    }

    public void setSearchedUsername(String searchedUsername) {
        this.searchedUsername = searchedUsername;
    }

    public Collection<String> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Collection<String> searchResults) {
        this.searchResults = searchResults;
    }
}
