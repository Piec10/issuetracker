package com.piec10.issuetracker.user;


import com.piec10.issuetracker.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class FormUser {

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String username;

    @ValidEmail
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String email;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String password;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String matchingPassword;


}
