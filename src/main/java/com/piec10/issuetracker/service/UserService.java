package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    public List<User> findAll();

    public User findByUsername(String username);

    public User findByEmail(String email);

    public boolean exists(String username);
}
