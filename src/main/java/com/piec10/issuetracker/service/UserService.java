package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.user.FormUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findByUsername(String username);

    User findByEmail(String email);

    boolean exists(String username);

    void save(FormUser formUser);

    void deleteById(String theId);

    void changePassword(String username, String newPassword);
}
