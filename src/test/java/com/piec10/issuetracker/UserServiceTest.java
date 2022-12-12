package com.piec10.issuetracker;

import com.piec10.issuetracker.dao.RoleRepository;
import com.piec10.issuetracker.dao.UserRepository;
import com.piec10.issuetracker.entity.Role;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import com.piec10.issuetracker.user.FormUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> capturedUser;

    private FormUser formUser;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Test
    public void createNewValidUserService() {

        formUser = new FormUser();
        formUser.setUsername("username");
        formUser.setEmail("email@email.com");
        formUser.setPassword("password");

        userService.save(formUser);

        verify(userRepository).save(capturedUser.capture());

        assertEquals(formUser.getUsername(), capturedUser.getValue().getUsername(), "Username should match");
        assertEquals(formUser.getEmail(), capturedUser.getValue().getEmail(), "User email should match");

        assertTrue(passwordEncoder.matches(formUser.getPassword(), capturedUser.getValue().getPassword()), "User encoded password should match");

        assertNotNull(capturedUser.getValue().getCreatedAt(), "Issue creation date should not be null");
        assertTrue(new Date().getTime() > capturedUser.getValue().getCreatedAt().getTime(), "User creation date should be older than current date");

        assertNull(capturedUser.getValue().getIssues(), "New user should not have any open issues");

        assertEquals(1, capturedUser.getValue().getRoles().size(), "New user should have one default role");
        assertEquals("ROLE_USER", capturedUser.getValue().getRoles().stream().toList().get(0).getName(), "New user should have default role 'ROLE_USER'");
    }


}
