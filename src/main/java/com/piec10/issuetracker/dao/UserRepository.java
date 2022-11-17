package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    public User findByEmail(String email);

    
}
