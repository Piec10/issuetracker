package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.RoleRepository;
import com.piec10.issuetracker.dao.UserRepository;
import com.piec10.issuetracker.entity.Role;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);

    }

    @Override
    public boolean exists(String username) {
        return userRepository.existsById(username);
    }

    @Override
    public void save(FormUser formUser) {
        User user = new User();
        user.setUsername(formUser.getUsername());
        user.setPassword(passwordEncoder.encode(formUser.getPassword()));
        user.setEmail(formUser.getEmail());
        user.setCreatedAt(new Date());
        user.setEnabled(1);

        // give user default role of "user"
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        // save user in the database
        userRepository.save(user);
    }

    @Override
    public void deleteById(String theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public void changePassword(String username, String newPassword) {

        User user = findByUsername(username);
        if(user != null){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
