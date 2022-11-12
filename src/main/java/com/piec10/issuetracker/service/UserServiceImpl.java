package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.UserDAO;
import com.piec10.issuetracker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public List<User> findAll() {
        return userDAO.findAll();
    }
}
