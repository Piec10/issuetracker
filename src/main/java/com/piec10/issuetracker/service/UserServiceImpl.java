package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.UserRepository;
import com.piec10.issuetracker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {

        Optional<User> user = userRepository.findById(username);

        if(user.isPresent()){
            return user.get();
        }
        else return null;
    }
}
