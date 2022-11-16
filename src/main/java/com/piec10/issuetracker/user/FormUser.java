package com.piec10.issuetracker.user;


import com.piec10.issuetracker.validation.FieldMatch;
import com.piec10.issuetracker.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
public class FormUser {

    @NotNull(message = "User name is required")
    @Size(min = 1, message = "User name is required")
    private String username;

    @ValidEmail
    @NotNull(message = "Email is required")
    @Size(min = 1, message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 1, message = "Password is required")
    private String password;

    @NotNull(message = "Password is required")
    @Size(min = 1, message = "Password is required")
    private String matchingPassword;

    public FormUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
