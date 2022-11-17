package com.piec10.issuetracker.service;

import com.piec10.issuetracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll();

    public User findByUsername(String username);
}
