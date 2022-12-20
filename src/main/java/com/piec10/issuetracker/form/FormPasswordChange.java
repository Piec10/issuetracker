package com.piec10.issuetracker.form;

import com.piec10.issuetracker.validation.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "matchingNewPassword", message = "The password fields must match")
public class FormPasswordChange {

    private String username;
    private String oldPassword;

    @NotNull(message = "New password is required")
    @Size(min = 1, message = "New password is required")
    private String newPassword;

    @NotNull(message = "New password is required")
    @Size(min = 1, message = "New password is required")
    private String matchingNewPassword;

    public FormPasswordChange() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMatchingNewPassword() {
        return matchingNewPassword;
    }

    public void setMatchingNewPassword(String matchingNewPassword) {
        this.matchingNewPassword = matchingNewPassword;
    }
}
