package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.User;

import java.util.List;

public interface UserDAO {

    public List<User> findAll();
}
